package com.example.payroll.domain.contract.port.out;

import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.employee.model.Employee;
import java.util.Collection;
import java.util.Optional;

public interface ContractRepository {
    Contract addContract(Contract contract, Employee employee);

    Collection<Contract> overlap(Employee employee, Contract contract);

    Optional<Contract> findContractById(Long id);
}
