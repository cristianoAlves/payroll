package com.example.payroll.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.example.payroll.adapters.outbound.persistence.employee.entity.BankAccountEntity;
import com.example.payroll.adapters.outbound.persistence.employee.entity.EmployeeEntity;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.employee.model.BankAccount;
import com.example.payroll.domain.employee.model.Employee;
import com.example.payroll.domain.employee.port.in.EmployeeUseCase;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeManagementIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeUseCase useCase;

    @Test
    void shouldCreateAndReturnEmployee() {
        Employee emp = createEmployee();


        var response = restTemplate.postForEntity("/employees", emp, Employee.class);

        assertResponse(response, HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertEmployee(response.getBody(), emp);
        assertBankAccount(response.getBody(), emp);
        assertContract(response.getBody(), emp);
    }

    @Test
    void shouldGetBadRequestWhenSavingDuplicatesEmployees() {
        Employee emp = new Employee(null, "name", "cpf-",
            new BankAccount("account", "branch"),
            new Contract(null, new BigDecimal(1000L), LocalDate.now(), LocalDate.now(), true)
        );


        restTemplate.postForEntity("/employees", emp, Employee.class);
        var response = restTemplate.postForEntity("/employees", emp, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Cannot be more than one Employee with the same cpf [cpf-]");
    }



    @Test
    void shouldReturnAllEmployees() {
        Employee emp1 = createEmployee();
        Employee emp2 = createEmployee();

        useCase.saveEmployee(emp1);
        useCase.saveEmployee(emp2);

        var response = restTemplate.getForEntity("/employees", CollectionModel.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody().getContent()).hasSize(2);
    }

    @Test
    void shouldReturnOneEmployee() {
        Employee emp1 = createEmployee();
        Employee expected = useCase.saveEmployee(emp1);

        var response = restTemplate.getForEntity(String.format("/employees/%s", expected.id()), Employee.class);
        assertResponse(response, HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertEmployee(response.getBody(), expected);
        assertBankAccount(response.getBody(), expected);
        assertContract(response.getBody(), expected);
    }

    @Test
    void addingContractToEmployee() {
        Employee employee = new Employee(null, "name", "cpf-" + RandomUtil.getPositiveInt(),
            null,
            null);

        Employee savedEmployee = useCase.saveEmployee(employee);
        Contract contract = new Contract(null, new BigDecimal(1000L), LocalDate.now(), LocalDate.now(), true);
        Employee expected = savedEmployee.assignContract(contract);

        var response = restTemplate.patchForObject(String.format("/employees/%s/contract", savedEmployee.id()), contract, Employee.class);
        assertThat(response).isNotNull();
        assertEmployee(response, expected);
        assertContract(response, expected);
    }

    @Test
    void addingBankAccountToEmployee() {
        Employee employee = new Employee(null, "name", "cpf-" + RandomUtil.getPositiveInt(),
            null,
            null);

        Employee savedEmployee = useCase.saveEmployee(employee);
        BankAccount bankAccount = new BankAccount("account", "branch");
        Employee expected = savedEmployee.assignBankAccount(bankAccount);

        var response = restTemplate.patchForObject(String.format("/employees/%s/bank-account", savedEmployee.id()), bankAccount, Employee.class);
        assertThat(response).isNotNull();
        assertEmployee(response, expected);
        assertBankAccount(response, expected);
    }

    private static void assertContract(Employee response, Employee emp) {
        assertThat(response.id()).isNotNull();
        assertThat(response.contract().endDate()).isEqualTo(emp.contract().endDate());
        assertThat(response.contract().startDate()).isEqualTo(emp.contract().startDate());
        assertThat(response.contract().salary()).isEqualTo(emp.contract().salary());
        assertThat(response.contract().active()).isEqualTo(emp.contract().active());
    }

    private static void assertBankAccount(Employee response, Employee emp) {
        assertThat(response.bankAccount().account()).isEqualTo(emp.bankAccount().account());
        assertThat(response.bankAccount().branch()).isEqualTo(emp.bankAccount().branch());
    }

    private static void assertEmployee(Employee response, Employee emp) {
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo(emp.name());
        assertThat(response.cpf()).isEqualTo(emp.cpf());
    }

    private static void assertResponse(ResponseEntity<Employee> response, HttpStatus status) {
        assertThat(response.getStatusCode()).isEqualTo(status);
        assertThat(response.getBody()).isNotNull();
    }

    private static Employee createEmployee() {
        return new Employee(null, "name", "cpf-"+ RandomUtil.getPositiveInt(),
            new BankAccount("account", "branch"),
            new Contract(null, new BigDecimal(1000L), LocalDate.now(), LocalDate.now(), true)
        );
    }
}