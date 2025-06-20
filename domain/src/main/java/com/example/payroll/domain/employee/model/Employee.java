package com.example.payroll.domain.employee.model;

public record Employee(
    Long id,
    String name,
    String cpf,
    BankAccount bankAccount
) {

    @SuppressWarnings("checkstyle:hiddenfield")
    public Employee assignBankAccount(BankAccount bankAccount) {
        return new Employee(this.id, this.name, this.cpf, bankAccount);
    }
}
