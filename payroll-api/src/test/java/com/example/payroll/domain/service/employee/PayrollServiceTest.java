package com.example.payroll.domain.service.employee;

import com.example.payroll.domain.entity.Employee;
import com.example.payroll.domain.repository.EmployeeRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PayrollServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService payrollService;

    @Test
    void shouldReturnEmployeeById() {
        Employee emp = Employee.builder()
            .id(1L)
            .name("Alice")
            .role("developer")
            .build();
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));

        Employee result = payrollService.getById(1L);
        Assertions.assertEquals("Alice", result.getName());
    }
}
