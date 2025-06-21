package com.example.payroll.application.services;

import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.employee.exception.EmployeeNotFoundException;
import com.example.payroll.domain.employee.exception.EmployeeWithSameCpfException;
import com.example.payroll.domain.employee.exception.PayrollGenericException;
import com.example.payroll.domain.employee.model.BankAccount;
import com.example.payroll.domain.employee.model.Employee;
import com.example.payroll.domain.employee.port.in.EmployeeUseCase;
import com.example.payroll.domain.employee.port.out.EmployeeRepository;
import java.util.Collection;
import java.util.Objects;
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
            if (Objects.nonNull(e.getMessage()) && e.getMessage().toUpperCase().contains("UK_EMPLOYEE_CPF")) {
                log.error("Cannot be more than one Employee with the same cpf [{}]. Error: [{}]", employee.cpf(), e.getMessage());
                throw new EmployeeWithSameCpfException(employee.cpf());
            }
            log.error("Unexpected data integrity violation while saving employee. Error: [{}]", e.getMessage());
            throw new PayrollGenericException("Unexpected data integrity violation while saving employee.", e.getMessage());
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
    public Employee assignBankAccount(BankAccount bankAccount, Long id) {
        log.info("Assigning Bank Account [{}] to Employee with id {}", bankAccount, id);
        Employee updatedEmployee = saveEmployee(getById(id).assignBankAccount(bankAccount));
        log.info("Employee [{}] was updated with bank account [{}]", updatedEmployee.id(), bankAccount);
        return updatedEmployee;
    }

    @Override
    public Employee assignContracts(Collection<Contract> contracts, Long id) {
        log.info("Assigning contracts [{}] to Employee with id {}", contracts, id);
        Employee updatedEmployee = saveEmployee(getById(id).assignContracts(contracts));
        log.info("Employee [{}] was updated with contracts [{}]", updatedEmployee.id(), contracts);
        return updatedEmployee;
    }
}
