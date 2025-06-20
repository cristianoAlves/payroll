package com.example.payroll.domain.contract.port.in;

import com.example.payroll.domain.contract.model.Contract;

public interface ContractValidationUseCase {

    Contract overlap(Contract contract);

    boolean isExpired(Contract contract);
}
