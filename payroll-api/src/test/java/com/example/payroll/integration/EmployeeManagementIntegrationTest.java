package com.example.payroll.integration;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.employee.model.BankAccount;
import com.example.payroll.domain.employee.model.Employee;
import com.example.payroll.domain.employee.port.in.EmployeeUseCase;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeUseCase useCase;

    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalDate END_DATE = START_DATE.plusDays(10);

    @Test
    void shouldCreateAndReturnEmployee() {
        Employee emp = createEmployeeFull();

        var response = restTemplate.postForEntity("/employees", emp, Employee.class);

        assertResponse(response, HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertEmployee(response.getBody(), emp);
        assertBankAccount(response.getBody(), emp);
    }

    @Test
    void shouldGetErrorWhenCreatingAnEmployeeWithConflictOnContracts() {
        List<Contract> contracts = List.of(createContract(START_DATE, END_DATE), createContract(START_DATE, END_DATE));
        Employee emp = createEmployee(contracts);

        var response = restTemplate.postForEntity("/employees", emp, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Some of the given contracts overlap with each other [{Contract[id=null");
    }

    @Test
    void shouldGetBadRequestWhenSavingDuplicatesEmployees() {
        Employee emp = createEmployeeFull();

        restTemplate.postForEntity("/employees", emp, Employee.class);
        var response = restTemplate.postForEntity("/employees", emp, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Cannot be more than one Employee with the same cpf [");
    }

    @Test
    void shouldReturnAllEmployees() {
        Employee emp1 = createEmployeeFull();
        Employee emp2 = createEmployeeFull();

        useCase.saveEmployee(emp1);
        useCase.saveEmployee(emp2);

        var response = restTemplate.getForEntity("/employees", CollectionModel.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody().getContent()).hasSize(2);
    }

    @Test
    void shouldReturnOneEmployee() {
        Employee emp1 = createEmployeeFull();
        Employee expected = useCase.saveEmployee(emp1);

        var response = restTemplate.getForEntity(String.format("/employees/%s", expected.id()), Employee.class);
        assertResponse(response, HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertEmployee(response.getBody(), expected);
        assertBankAccount(response.getBody(), expected);
    }

    @Test
    void addingBankAccountToEmployee() {
        Employee employee = createEmployee((BankAccount) null);

        Employee savedEmployee = useCase.saveEmployee(employee);
        BankAccount bankAccount = new BankAccount("account", "branch");
        Employee expected = savedEmployee.assignBankAccount(bankAccount);

        var response = restTemplate.patchForObject(String.format("/employees/%s/bank-account", savedEmployee.id()), bankAccount, Employee.class);
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

        var response = restTemplate.patchForObject(String.format("/employees/%s/contracts", savedEmployee.id()), contractsToBeAdded, Employee.class);
        assertThat(response).isNotNull();
        assertEmployee(response, expected);
        assertBankAccount(response, expected);
    }

    @Test
    void addingOverlappedContractsShouldThrowErrorWhenComparingWithSavedContracts() {
        List<Contract> contractsToBeAdded = List.of(createContract(START_DATE, END_DATE));
        validatingOverlappedContracts(contractsToBeAdded, "Some of these contracts overlap with already saved ones. [{Contract[id=null");
    }

    @Test
    void addingOverlappedContractsShouldThrowErrorWhenComparingWithEachOtherFromRequest() {
        List<Contract> contractsToBeAdded = List.of(
            createContract(START_DATE, END_DATE),
            createContract(START_DATE, END_DATE));
        validatingOverlappedContracts(contractsToBeAdded, "Some of the given contracts overlap with each other [{Contract[id=null");
    }

    @Test
    void addingNullContractsShouldGetError() {
        var response = assignContractToEmployee(null, createEmployee(10L));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Required request body is missing:");
    }

    @Test
    void addingEmptyContractsShouldGetError() {
        var response = assignContractToEmployee(List.of(), createEmployee(10L));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("At least one contract is required.");
    }

    private void validatingOverlappedContracts(List<Contract> contractsToBeAdded, String expectedErrorMessage) {
        Employee employee = createEmployeeFull();
        Employee savedEmployee = useCase.saveEmployee(employee);

        var response = assignContractToEmployee(contractsToBeAdded, savedEmployee);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains(expectedErrorMessage);
    }

    private ResponseEntity<String> assignContractToEmployee(List<Contract> contractsToBeAdded, Employee savedEmployee) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return restTemplate.exchange(
            String.format("/employees/%s/contracts", savedEmployee.id()),
            HttpMethod.PATCH,
            new HttpEntity<>(contractsToBeAdded, headers),
            String.class
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
}