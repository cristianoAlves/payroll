package com.example.payroll.domain.employee.model;

import com.example.payroll.domain.contract.model.Contract;

public record Employee(
    Long id,
    String name,
    String cpf,
    BankAccount bankAccount,
    Contract contract
) {

    @SuppressWarnings("checkstyle:hiddenfield")
    public Employee assignContract(Contract contract) {
        return new Employee(this.id, this.name, this.cpf, this.bankAccount, contract);
    }

    @SuppressWarnings("checkstyle:hiddenfield")
    public Employee assignBankAccount(BankAccount bankAccount) {
        return new Employee(this.id, this.name, this.cpf, bankAccount, this.contract);
    }
}
