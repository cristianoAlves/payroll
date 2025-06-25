package com.example.payroll.domain.contract.port.out;

import com.example.payroll.domain.contract.model.Contract;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ContractRepository {

    Optional<Contract> findContractById(Long id);

    List<Contract> findOverlappingContracts(Long employeeId, LocalDate startDate, LocalDate endDate);
}
