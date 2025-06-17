package com.example.payroll.application.services;

import com.example.payroll.domain.employee.exception.EmployeeNotFoundException;
import com.example.payroll.domain.employee.model.Employee;
import com.example.payroll.domain.employee.port.in.EmployeeUseCase;
import com.example.payroll.domain.employee.port.out.EmployeeRepository;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService implements EmployeeUseCase {

    private final EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        log.info("Saving employee {}", employee);
        return employeeRepository.save(employee);
    }

    public Collection<Employee> getAllEmployees() {
        log.info("Getting all employees");
        Collection<Employee> all = employeeRepository.findAll();
        log.info("{} employees have been found", all.size());
        return all;
    }

    public Employee getById(Long id) {
        log.info("Attempt to retrieve Employee with id {}", id);
        return employeeRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Employee {} has not been found", id);
                return new EmployeeNotFoundException(id);
            });
    }
}
