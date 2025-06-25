package com.example.payroll.adapters.inbound.rest.dto;

import com.example.payroll.domain.contract.model.Contract;
import java.util.Collection;

public record ContractRequest(
    Long employeeId,
    Collection<Contract> contracts
) implements PayrollRequest {

}
