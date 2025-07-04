package com.example.payroll.domain.employee.exception;

import com.example.payroll.domain.contract.model.Contract;
import java.util.List;
import java.util.Map;

public class ContractHasOverlapException extends RuntimeException {

    private final Map<String, List<Contract>> contracts;

    public ContractHasOverlapException(Map<String, List<Contract>> contracts, String message) {
        super(message);
        this.contracts = contracts;
    }

    public Map<String, List<Contract>> getContracts() {
        return contracts;
    }
}
