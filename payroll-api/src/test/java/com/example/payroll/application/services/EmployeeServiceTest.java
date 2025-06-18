package com.example.payroll.application.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.employee.exception.EmployeeNotFoundException;
import com.example.payroll.domain.employee.model.BankAccount;
import com.example.payroll.domain.employee.model.Employee;
import com.example.payroll.domain.employee.port.out.EmployeeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {
    private static final String EMP_NAME = "Alice";

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService payrollService;

    @Test
    void shouldReturnEmployeeById() {
        Employee emp = createEmployee(1L, EMP_NAME);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));

        Employee result = payrollService.getById(1L);
        Assertions.assertEquals("Alice", result.name());
    }

    @Test
    public void shouldSaveAnEmployee() {
        Employee emp = createEmployee(1L, EMP_NAME);
        Mockito.when(employeeRepository.save(emp)).thenReturn(emp);

        Employee result = payrollService.saveEmployee(emp);
        Assertions.assertEquals(EMP_NAME, result.name());
    }

    @Test
    public void shouldUpdateAnEmployee() {
        Employee emp = createEmployee(1L, EMP_NAME);
        Employee expected = createEmployee(1L, "novo");

        Mockito.when(employeeRepository.findById(emp.id())).thenReturn(Optional.of(emp));
        Mockito.when(employeeRepository.save(emp)).thenReturn(expected);

        Employee result = payrollService.updateEmployee(emp);
        Assertions.assertEquals(expected.name(), result.name());
    }


    @Test
    public void shouldGetListOfEmployees() {
        Employee e1 = createEmployee(1L, EMP_NAME);
        Employee e2 = createEmployee(2L, EMP_NAME);

        Mockito.when(employeeRepository.save(e1)).thenReturn(e1);
        Mockito.when(employeeRepository.save(e2)).thenReturn(e2);

        Employee result1 = payrollService.saveEmployee(e1);
        Employee result2 = payrollService.saveEmployee(e2);
        List<Employee> expectedList = Arrays.asList(result1, result2);

        Mockito.when(employeeRepository.findAll()).thenReturn(expectedList);

        assertThat(payrollService.getAllEmployees())
            .hasSize(2)
            .containsExactlyInAnyOrderElementsOf(expectedList);
    }

    @Test
    public void shouldAddContractToEmployee() {
        Employee emp = createEmployee(1L, EMP_NAME);
        LocalDate localDate = LocalDate.now();
        Contract contract = new Contract(10L, new BigDecimal(5000L), localDate, localDate.plusDays(10), true);

        Employee expected = emp.assignContract(contract);
        Mockito.when(employeeRepository.findById(emp.id())).thenReturn(Optional.of(emp));
        Mockito.when(employeeRepository.save(emp)).thenReturn(expected);

        Employee result = payrollService.assignContract(contract, emp.id());
        assertThat(expected.name()).isEqualTo(result.name());
        assertThat(expected.cpf()).isEqualTo(result.cpf());
        assertThat(expected.contract().active()).isEqualTo(result.contract().active());
        assertThat(expected.contract().salary()).isEqualTo(result.contract().salary());
        assertThat(expected.contract().startDate()).isEqualTo(result.contract().startDate());
        assertThat(expected.contract().endDate()).isEqualTo(result.contract().endDate());
    }

    @Test
    public void shouldAddBankAccountToEmployee() {
        Employee emp = createEmployee(1L, EMP_NAME);
        BankAccount bankAccount = new BankAccount("123", "1");

        Employee expected = emp.assignBankAccount(bankAccount);
        Mockito.when(employeeRepository.findById(emp.id())).thenReturn(Optional.of(emp));
        Mockito.when(employeeRepository.save(emp)).thenReturn(expected);

        Employee result = payrollService.assignBankAccount(bankAccount, emp.id());
        assertThat(expected.name()).isEqualTo(result.name());
        assertThat(expected.cpf()).isEqualTo(result.cpf());
        assertThat(expected.bankAccount().account()).isEqualTo(result.bankAccount().account());
        assertThat(expected.bankAccount().branch()).isEqualTo(result.bankAccount().branch());
    }

    @Test
    public void shouldRemoveEmployee() {
        Employee emp = createEmployee(1L, EMP_NAME);
        Mockito.when(employeeRepository.findById(emp.id())).thenReturn(Optional.of(emp));

        payrollService.removeEmployee(1L);
        verify(employeeRepository).removeEmployee(1L);
    }

    @Test
    public void shouldThrowExemptionWhenGettingUnexistEmployee() {
        try {
            payrollService.getById(1L);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(EmployeeNotFoundException.class);
            assertThat(e.getMessage()).isEqualTo("Could not find employee 1");
        }

    }

    private Employee createEmployee(long id, String name) {
        return new Employee(id, name, "cpf123", null, null);
    }
}