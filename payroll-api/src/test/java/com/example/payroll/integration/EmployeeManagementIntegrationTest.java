package com.example.payroll.integration;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.example.payroll.adapters.inbound.rest.dto.PayrollErrorResponse;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.employee.model.BankAccount;
import com.example.payroll.domain.employee.model.Employee;
import com.example.payroll.domain.employee.port.in.EmployeeUseCase;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {
    "DELETE FROM contract",
    "DELETE FROM employee",
    "DELETE FROM bank_account"
},
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
    config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
)
public class EmployeeManagementIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private EmployeeUseCase useCase;

    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalDate END_DATE = START_DATE.plusDays(10);

    @Test
    void shouldCreateAndReturnEmployee() {
        Employee emp = createEmployeeFull();

        var response = testRestTemplate.postForEntity("/employees", emp, Employee.class);

        assertResponse(response, HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertEmployee(response.getBody(), emp);
        assertBankAccount(response.getBody(), emp);
    }

    @Test
    void shouldGetErrorWhenCreatingAnEmployeeWithConflictOnContracts() {
        Contract contract = createContract(START_DATE, END_DATE);
        List<Contract> contracts = List.of(contract, contract);
        Employee emp = createEmployee(contracts);
        Map<String, List<Contract>> expectedMetadata = Map.of(contract.toString(), contracts);

        var response = testRestTemplate.postForEntity("/employees", emp, PayrollErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().createdAt()).isNotNull();
        assertThat(response.getBody().message()).contains("Some of the given contracts overlap with each other");
        assertThat(response.getBody().metadata()).containsExactlyInAnyOrderEntriesOf(expectedMetadata);
    }

    @Test
    void shouldGetBadRequestWhenSavingDuplicatesEmployees() {
        Employee emp = createEmployeeFull();

        testRestTemplate.postForEntity("/employees", emp, Employee.class);
        var response = testRestTemplate.postForEntity("/employees", emp, PayrollErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).contains("Cannot be more than one Employee with the same cpf [");
        assertThat(response.getBody().metadata()).isEmpty();
        assertThat(response.getBody().createdAt()).isNotNull();
    }

    @Test
    void shouldReturnAllEmployees() {
        Employee emp1 = createEmployeeFull();
        Employee emp2 = createEmployeeFull();

        useCase.saveEmployee(emp1);
        useCase.saveEmployee(emp2);

        var response = testRestTemplate.getForEntity("/employees", CollectionModel.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody().getContent()).hasSize(2);
    }

    @Test
    void shouldReturnOneEmployee() {
        Employee emp1 = createEmployeeFull();
        Employee expected = useCase.saveEmployee(emp1);

        var response = testRestTemplate.getForEntity(String.format("/employees/%s", expected.id()), Employee.class);
        assertResponse(response, HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertEmployee(response.getBody(), expected);
        assertBankAccount(response.getBody(), expected);
    }

    @Test
    void shouldGetErrorWhenEmployeeWasNotFound() {
        var response = testRestTemplate.getForEntity(String.format("/employees/%s", 10L), PayrollErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).contains("Could not find employee 10");
        assertThat(response.getBody().metadata()).isEmpty();
        assertThat(response.getBody().createdAt()).isNotNull();
    }

    @Test
    void addingBankAccountToEmployee() {
        Employee employee = createEmployee((BankAccount) null);

        Employee savedEmployee = useCase.saveEmployee(employee);
        BankAccount bankAccount = new BankAccount("account", "branch");
        Employee expected = savedEmployee.assignBankAccount(bankAccount);

        var response = testRestTemplate.patchForObject(String.format("/employees/%s/bank-account", savedEmployee.id()), bankAccount, Employee.class);
        assertThat(response).isNotNull();
        assertEmployee(response, expected);
        assertBankAccount(response, expected);
    }

    @Test
    void addingContractsToEmployee() {
        Employee employee = createEmployeeFull();
        Employee savedEmployee = useCase.saveEmployee(employee);
        List<Contract> contractsToBeAdded = List.of(createContract(START_DATE.minusDays(50), END_DATE.minusDays(30)));
        Employee expected = savedEmployee.assignContracts(contractsToBeAdded);

        var response = testRestTemplate.patchForObject(String.format("/employees/%s/contracts", savedEmployee.id()), contractsToBeAdded, Employee.class);
        assertThat(response).isNotNull();
        assertEmployee(response, expected);
        assertBankAccount(response, expected);
    }

    @Test
    void addingOverlappedContractsShouldThrowErrorWhenComparingWithSavedContracts() {
        Employee employee = createEmployeeFull();
        Contract contract = createContract(START_DATE, END_DATE);
        Employee savedEmployee = useCase.saveEmployee(employee);
        List<Contract> contractsToBeAdded = List.of(contract);
        Long expectedContractId = ((List<Contract>) savedEmployee.contracts()).get(0).id();

        var response = assignContractToEmployee(contractsToBeAdded, savedEmployee);
        Map<String, List<Contract>> expectedMetadata = Map.of(contract.toString(), List.of(createContract(expectedContractId, START_DATE, END_DATE)));

        validatingOverlappedContracts(response, expectedMetadata, "Some of these contracts overlap with already saved ones.");
    }

    @Test
    void addingOverlappedContractsShouldThrowErrorWhenComparingWithEachOtherFromRequest() {
        Employee employee = createEmployeeFull();
        Employee savedEmployee = useCase.saveEmployee(employee);
        Contract contract = createContract(START_DATE, END_DATE);
        List<Contract> contractsToBeAdded = List.of(contract, contract);
        Map<String, List<Contract>> expectedMetadata = Map.of(contract.toString(), contractsToBeAdded);

        var response = assignContractToEmployee(contractsToBeAdded, savedEmployee);

        validatingOverlappedContracts(response, expectedMetadata, "Some of the given contracts overlap with each other");
    }

    @Test
    void addingNullContractsShouldGetError() {
        var response = assignContractToEmployee(null, createEmployee(10L));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).contains("Required request body is missing:");
        assertThat(response.getBody().metadata()).isEmpty();
        assertThat(response.getBody().createdAt()).isNotNull();
    }

    @Test
    void addingEmptyContractsShouldGetError() {
        var response = assignContractToEmployee(List.of(), createEmployee(10L));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).contains("At least one contract is required.");
        assertThat(response.getBody().metadata()).isEmpty();
        assertThat(response.getBody().createdAt()).isNotNull();
    }

    private void validatingOverlappedContracts(ResponseEntity<PayrollErrorResponse> response, Map<String, List<Contract>> expectedMetadata, String expectedErrorMessage) {

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().createdAt()).isNotNull();
        assertThat(response.getBody().message()).contains(expectedErrorMessage);
        assertThat(response.getBody().metadata()).containsExactlyInAnyOrderEntriesOf(expectedMetadata);
    }

    private ResponseEntity<PayrollErrorResponse> assignContractToEmployee(List<Contract> contractsToBeAdded, Employee savedEmployee) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return testRestTemplate.exchange(
            String.format("/employees/%s/contracts", savedEmployee.id()),
            HttpMethod.PATCH,
            new HttpEntity<>(contractsToBeAdded, headers),
            PayrollErrorResponse.class
        );
    }

    private void assertBankAccount(Employee response, Employee emp) {
        assertThat(response.bankAccount().account()).isEqualTo(emp.bankAccount().account());
        assertThat(response.bankAccount().branch()).isEqualTo(emp.bankAccount().branch());
    }

    private void assertEmployee(Employee response, Employee emp) {
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo(emp.name());
        assertThat(response.cpf()).isEqualTo(emp.cpf());
        assertThat(response.contracts()).hasSameSizeAs(emp.contracts());
    }

    private void assertResponse(ResponseEntity<Employee> response, HttpStatus status) {
        assertThat(response.getStatusCode()).isEqualTo(status);
        assertThat(response.getBody()).isNotNull();
    }

    private Employee createEmployeeFull() {
        return new Employee(null, "name", "cpf-" + RandomUtil.getPositiveInt(),
            createBankAccount(), List.of(createContract(START_DATE, END_DATE))
        );
    }

    private Employee createEmployee(Long id) {
        return new Employee(id, "name", "cpf-" + RandomUtil.getPositiveInt(),
            createBankAccount(), List.of(createContract(START_DATE, END_DATE))
        );
    }

    private Employee createEmployee(BankAccount bankAccount) {
        return new Employee(null, "name", "cpf-" + RandomUtil.getPositiveInt(),
            bankAccount, List.of(createContract(START_DATE, END_DATE))
        );
    }

    private Employee createEmployee(List<Contract> contracts) {
        return new Employee(null, "name", "cpf-" + RandomUtil.getPositiveInt(),
            createBankAccount(), contracts);
    }

    private BankAccount createBankAccount() {
        return new BankAccount("account", "branch");
    }

    private Contract createContract(LocalDate startDate, LocalDate endDate) {
        return new Contract(null, new BigDecimal(1000), startDate, endDate, true);
    }

    private Contract createContract(Long id, LocalDate startDate, LocalDate endDate) {
        return new Contract(id, new BigDecimal(1000), startDate, endDate, true);
    }
}