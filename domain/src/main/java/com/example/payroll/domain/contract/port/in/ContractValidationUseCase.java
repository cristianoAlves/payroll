package com.example.payroll.domain.contract.port.in;

import com.example.payroll.domain.contract.model.Contract;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ContractValidationUseCase {

    Map<Contract, List<Contract>> overlap(Collection<Contract> contracts, Long employeeId);

    Map<Contract, List<Contract>> overlap(Collection<Contract> contracts);

    boolean isExpired(Contract contract);
}
