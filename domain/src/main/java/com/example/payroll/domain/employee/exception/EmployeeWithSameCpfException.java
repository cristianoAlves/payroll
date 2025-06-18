package com.example.payroll.domain.employee.exception;

public class EmployeeWithSameCpfException extends RuntimeException {

    public EmployeeWithSameCpfException(String cpf) {
        super(String.format("Cannot be more than one Employee with the same cpf [%s]", cpf));
    }
}
