package com.example.payroll.domain.service.employee;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.payroll.domain.entity.Employee;
import com.example.payroll.domain.repository.EmployeeRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService payrollService;

    @Test
    void shouldReturnEmployeeById() {
        Employee emp = createEmployee(1L);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));

        Employee result = payrollService.getById(1L);
        Assertions.assertEquals("Alice", result.getName());
    }

    @Test
    public void shouldSaveAnEmployee() {
        Employee emp = createEmployee(1L);
        Mockito.when(employeeRepository.save(emp)).thenReturn(emp);

        Employee result = payrollService.saveEmployee(emp);
        Assertions.assertEquals("Alice", result.getName());
    }

    @Test
    public void shouldGetListOfEmployees() {
        Employee e1 = createEmployee(1L);
        Employee e2 = createEmployee(2L);

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

    private Employee createEmployee(long id) {
        return Employee.builder()
            .id(id)
            .name("Alice")
            .role("developer")
            .build();
    }
}
