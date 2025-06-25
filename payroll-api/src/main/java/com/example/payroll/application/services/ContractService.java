package com.example.payroll.application.services;

import com.example.payroll.domain.contract.execption.ContractNotFoundException;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.contract.port.in.ContractUseCase;
import com.example.payroll.domain.contract.port.out.ContractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractService implements ContractUseCase {

    private final ContractRepository repository;

    @Override
    public Contract findOne(Long id) {
        log.info("Attempt to retrieve Contract with id {}", id);
        return repository.findContractById(id)
            .orElseThrow(() -> {
                log.error("Contract {} has not been found", id);
                return new ContractNotFoundException(id);
            });
    }
}
