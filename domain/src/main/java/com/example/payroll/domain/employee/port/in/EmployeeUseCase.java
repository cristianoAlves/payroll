package com.example.payroll.domain.employee.port.in;

import com.example.payroll.domain.employee.model.Employee;
import java.util.Collection;

public interface EmployeeUseCase {

    Employee saveEmployee(Employee employee);

    Collection<Employee> getAllEmployees();

    Employee getById(Long id);
}
