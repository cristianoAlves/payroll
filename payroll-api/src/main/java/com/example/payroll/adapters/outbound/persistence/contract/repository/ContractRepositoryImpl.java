package com.example.payroll.adapters.outbound.persistence.contract.repository;

import com.example.payroll.adapters.outbound.persistence.contract.entity.ContractEntity;
import com.example.payroll.adapters.outbound.persistence.contract.mapper.ContractMapper;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.contract.port.out.ContractRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ContractRepositoryImpl implements ContractRepository {

    private final ContractRepositoryJpa contractRepositoryJpa;
    private final ContractMapper mapper;

    @Override
    public Optional<Contract> findContractById(Long id) {
        return contractRepositoryJpa.findById(id)
            .map(mapper::fromEntity);
    }

    @Override
    public List<Contract> findOverlappingContracts(Long employeeId, LocalDate startDate, LocalDate endDate) {
        List<ContractEntity> overlappingContracts = contractRepositoryJpa.findOverlappingContracts(employeeId, startDate, endDate);
        return overlappingContracts.stream().map(mapper::fromEntity).toList();
    }
}
