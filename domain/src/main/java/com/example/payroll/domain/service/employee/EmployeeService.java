package com.example.payroll.domain.service.employee;

import com.example.payroll.domain.entity.Employee;
import com.example.payroll.domain.exception.EmployeeNotFoundException;
import com.example.payroll.domain.repository.EmployeeRepository;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        log.info("Saving employee {}", employee);
        return employeeRepository.save(employee);
    }

    public Collection<Employee> getAllEmployees() {
        log.info("Getting all employees");
        List<Employee> all = employeeRepository.findAll();
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
