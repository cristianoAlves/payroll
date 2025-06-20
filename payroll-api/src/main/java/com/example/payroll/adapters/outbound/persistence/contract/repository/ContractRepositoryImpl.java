package com.example.payroll.adapters.outbound.persistence.contract.repository;

import com.example.payroll.adapters.outbound.persistence.contract.entity.ContractEntity;
import com.example.payroll.adapters.outbound.persistence.contract.mapper.ContractMapper;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.contract.port.out.ContractRepository;
import com.example.payroll.domain.employee.model.Employee;
import java.util.Collection;
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
    public Contract addContract(Contract contract, Employee employee) {
        return mapper.fromEntity(contractRepositoryJpa.save(mapper.fromContract(contract.addEmployee(employee))));
    }

    @Override
    public Collection<Contract> overlap(Employee employee, Contract contract) {
        List<ContractEntity> contractEntityList = contractRepositoryJpa.findOverlappingContracts(employee.id(),
            contract.startDate(), contract.endDate());

        return null;
    }

    @Override
    public Optional<Contract> findContractById(Long id) {
        return contractRepositoryJpa.findById(id)
            .map(mapper::fromEntity);
    }
}
