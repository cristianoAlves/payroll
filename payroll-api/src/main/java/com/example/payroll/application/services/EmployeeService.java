package com.example.payroll.application.services;

import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.employee.exception.EmployeeNotFoundException;
import com.example.payroll.domain.employee.exception.EmployeeWithSameCpfException;
import com.example.payroll.domain.employee.model.BankAccount;
import com.example.payroll.domain.employee.model.Employee;
import com.example.payroll.domain.employee.port.in.EmployeeUseCase;
import com.example.payroll.domain.employee.port.out.EmployeeRepository;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService implements EmployeeUseCase {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        log.info("Saving employee {}", employee);
        try {
            return employeeRepository.save(employee);
        } catch (DataIntegrityViolationException e) {
            log.error("Cannot be more than one Employee with the same cpf [{}]. Error: [{}]", employee.cpf(), e.getMessage());
            throw new EmployeeWithSameCpfException(employee.cpf());
        }
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        //TODO: validate id
        getById(employee.id());
        return employeeRepository.save(employee);
    }

    @Override
    public Collection<Employee> getAllEmployees() {
        log.info("Getting all employees");
        Collection<Employee> all = employeeRepository.findAll();
        log.info("{} employees have been found", all.size());
        return all;
    }

    @Override
    public Employee getById(Long id) {
        log.info("Attempt to retrieve Employee with id {}", id);
        return employeeRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Employee {} has not been found", id);
                return new EmployeeNotFoundException(id);
            });
    }

    @Override
    public void removeEmployee(Long id) {
        log.info("Removing Employee with id {}", id);
        getById(id);
        employeeRepository.removeEmployee(id);
        log.info("Employee removed");
    }

    @Override
    public Employee assignContract(Contract contract, Long id) {
        log.info("Assigning Contract [{}] to Employee with id {}", contract, id);
        Employee toBeUpdated = getById(id).assignContract(contract);
        Employee savedEmployee = saveEmployee(toBeUpdated);
        log.info("Employee [{}] was updated with Contract [{}]", savedEmployee.id(), contract);
        return savedEmployee;
    }

    @Override
    public Employee assignBankAccount(BankAccount bankAccount, Long id) {
        log.info("Assigning Bank Account [{}] to Employee with id {}", bankAccount, id);
        Employee updatedEmployee = getById(id).assignBankAccount(bankAccount);
        saveEmployee(updatedEmployee);
        log.info("Employee [{}] was updated with bank account [{}]", updatedEmployee.id(), bankAccount);
        return updatedEmployee;
    }
}
