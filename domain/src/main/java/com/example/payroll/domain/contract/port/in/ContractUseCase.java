package com.example.payroll.domain.contract.port.in;

import com.example.payroll.domain.contract.model.Contract;

public interface ContractUseCase {

    Contract findOne(Long id);
}
