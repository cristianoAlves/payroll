package com.example.payroll.domain.employee.port.in;

import com.example.payroll.domain.employee.model.BankAccount;
import com.example.payroll.domain.employee.model.Employee;
import java.util.Collection;

public interface EmployeeUseCase {

    Employee saveEmployee(Employee employee);

    Employee updateEmployee(Employee employee);

    Collection<Employee> getAllEmployees();

    Employee getById(Long id);

    void removeEmployee(Long id);

    Employee assignBankAccount(BankAccount bankAccount, Long id);

}
