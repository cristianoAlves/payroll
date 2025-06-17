package com.example.payroll.domain.employee.port.out;

import com.example.payroll.domain.employee.model.Employee;
import java.util.Collection;
import java.util.Optional;

public interface EmployeeRepository {

    Employee save(Employee employee);

    Collection<Employee> findAll();

    Optional<Employee> findById(Long id);

}
