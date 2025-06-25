package com.example.payroll.adapters.inbound.rest.dto;

import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.employee.model.BankAccount;
import java.util.Collection;

public record EmployeeRequest(
    Long id,
    String name,
    String cpf,
    BankAccount bankAccount,
    Collection<Contract> contracts
) implements PayrollRequest {

}
