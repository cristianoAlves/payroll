package com.example.payroll.domain.employee.exception;

import com.example.payroll.domain.contract.model.Contract;
import java.util.List;
import java.util.Map;

public class ContractHasOverlapException extends RuntimeException {

    public ContractHasOverlapException(Map<Contract, List<Contract>> contracts, String message) {
        super(String.format(message + " [%s]", contracts));
    }
}
