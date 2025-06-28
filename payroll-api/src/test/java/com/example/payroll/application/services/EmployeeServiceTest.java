package com.example.payroll.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import com.example.payroll.domain.employee.exception.EmployeeNotFoundException;
import com.example.payroll.domain.employee.exception.EmployeeWithSameCpfException;
import com.example.payroll.domain.employee.exception.PayrollGenericException;
import com.example.payroll.domain.employee.model.BankAccount;
import com.example.payroll.domain.employee.model.Employee;
import com.example.payroll.domain.employee.port.out.EmployeeRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;

public class EmployeeServiceTest extends BaseServiceTest {

    private static final String EMP_NAME = "Alice";

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService payrollService;

    @Test
    void shouldReturnEmployeeById() {
        Employee emp = createEmployee(1L, EMP_NAME, createContract(null, LocalDate.now(), LocalDate.now().plusDays(10)));
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));

        Employee result = payrollService.getById(1L);
        assertThat(result.name()).isEqualTo(EMP_NAME);
        assertThat(result.contracts()).hasSize(1);
    }

    @Test
    public void shouldSaveAnEmployee() {
        Employee emp = createEmployee(1L, EMP_NAME, createContract(null, LocalDate.now(), LocalDate.now().plusDays(10)));
        Mockito.when(employeeRepository.save(emp)).thenReturn(emp);

        Employee result = payrollService.saveEmployee(emp);
        assertThat(result.name()).isEqualTo(EMP_NAME);
        assertThat(result.contracts()).hasSize(1);
    }

    @Test
    public void shouldGetErrorWhenSavingAnEmployeeWithSameCpf() {
        Employee emp1 = createEmployee(1L, EMP_NAME, createContract(null, LocalDate.now(), LocalDate.now().plusDays(10)));
        Employee emp2 = createEmployee(2L, EMP_NAME, createContract(null, LocalDate.now(), LocalDate.now().plusDays(10)));
        Mockito.when(employeeRepository.save(emp1)).thenReturn(emp1);

        Mockito.when(employeeRepository.save(emp2)).thenThrow(new DataIntegrityViolationException("UK_EMPLOYEE_CPF"));

        payrollService.saveEmployee(emp1);
        EmployeeWithSameCpfException exception = assertThrows(EmployeeWithSameCpfException.class,
            () -> payrollService.saveEmployee(emp2));
        assertThat(exception.getMessage()).isEqualTo("Cannot be more than one Employee with the same cpf [cpf123]");
    }

    @Test
    public void shouldGetGenericError() {
        Employee emp1 = createEmployee(1L, EMP_NAME, createContract(null, LocalDate.now(), LocalDate.now().plusDays(10)));
        Mockito.when(employeeRepository.save(emp1)).thenThrow(DataIntegrityViolationException.class);

        PayrollGenericException exception = assertThrows(PayrollGenericException.class,
            () -> payrollService.saveEmployee(emp1));
        assertThat(exception.getMessage()).isEqualTo("Unexpected error: Error: [Unexpected data integrity violation while saving employee.]. Message: null");
    }

    @Test
    public void shouldUpdateAnEmployee() {
        Employee emp = createEmployee(1L, EMP_NAME, createContract(null, LocalDate.now(), LocalDate.now().plusDays(10)));
        Employee expected = createEmployee(1L, "novo", createContract(null, LocalDate.now(), LocalDate.now().plusDays(10)));

        Mockito.when(employeeRepository.findById(emp.id())).thenReturn(Optional.of(emp));
        Mockito.when(employeeRepository.save(emp)).thenReturn(expected);

        Employee result = payrollService.updateEmployee(emp);
        Assertions.assertEquals(expected.name(), result.name());
    }

    @Test
    public void shouldGetListOfEmployees() {
        Employee e1 = createEmployee(1L, EMP_NAME, createContract(null, LocalDate.now(), LocalDate.now().plusDays(10)));
        Employee e2 = createEmployee(2L, EMP_NAME, createContract(null, LocalDate.now(), LocalDate.now().plusDays(10)));

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
    public void shouldAddBankAccountToEmployee() {
        Employee emp = createEmployee(1L, EMP_NAME, createContract(null, LocalDate.now(), LocalDate.now().plusDays(10)));
        BankAccount bankAccount = new BankAccount("123", "1");

        Employee expected = emp.assignBankAccount(bankAccount);
        Mockito.when(employeeRepository.findById(emp.id())).thenReturn(Optional.of(emp));
        Mockito.when(employeeRepository.save(expected)).thenReturn(expected);

        Employee result = payrollService.assignBankAccount(bankAccount, emp.id());
        assertThat(expected.name()).isEqualTo(result.name());
        assertThat(expected.cpf()).isEqualTo(result.cpf());
        assertThat(expected.bankAccount().account()).isEqualTo(result.bankAccount().account());
        assertThat(expected.bankAccount().branch()).isEqualTo(result.bankAccount().branch());
    }

    @Test
    public void shouldRemoveEmployee() {
        Employee emp = createEmployee(1L, EMP_NAME, createContract(null, LocalDate.now(), LocalDate.now().plusDays(10)));
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
}