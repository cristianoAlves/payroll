package com.example.payroll.domain.employee.model;

import com.example.payroll.domain.contract.model.Contract;

public record Employee(
    Long id,
    String name,
    BankAccount bankAccount,
    Contract contract
) {

}
