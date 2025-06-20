package com.example.payroll.domain.contract.port.in;

import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.employee.model.Employee;

public interface ContractUseCase {

    Contract assignContractToEmployee(Contract contract, Employee employee);

    Contract findOne(Long id);
}
