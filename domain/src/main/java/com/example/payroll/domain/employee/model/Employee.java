package com.example.payroll.domain.employee.model;

import com.example.payroll.domain.contract.model.Contract;
import java.util.Collection;

public record Employee(
    Long id,
    String name,
    String cpf,
    BankAccount bankAccount,
    Collection<Contract> contracts
) {

    @SuppressWarnings("checkstyle:hiddenField")
    public Employee assignBankAccount(BankAccount bankAccount) {
        return new Employee(this.id, this.name, this.cpf, bankAccount, this.contracts);
    }

    @SuppressWarnings("checkstyle:hiddenField")
    public Employee assignContracts(Collection<Contract> contracts) {
        this.contracts.addAll(contracts);
        return new Employee(this.id, this.name, this.cpf, this.bankAccount, this.contracts);
    }
}
