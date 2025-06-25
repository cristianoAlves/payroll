package com.example.payroll.domain.contract.execption;

public class ContractNotFoundException extends RuntimeException {

    public ContractNotFoundException(Long id) {
        super("Could not find contract " + id);
    }
}
