package com.example.payroll.application.services;

import com.example.payroll.domain.contract.execption.ContractNotFoundException;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.contract.port.in.ContractUseCase;
import com.example.payroll.domain.contract.port.out.ContractRepository;
import com.example.payroll.domain.employee.model.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractService implements ContractUseCase {

    private final ContractRepository repository;

    @Override
    public Contract assignContractToEmployee(Contract contract, Employee employee) {
        log.info("Assigning contract [{}] to employee [{}]", contract, employee);
        return repository.addContract(contract, employee);
    }

    @Override
    public Contract findOne(Long id) {
        log.info("Attempt to retrieve Contract with id {}", id);
        return repository.findContractById(id)
            .orElseThrow(() -> {
                log.error("Employee {} has not been found", id);
                return new ContractNotFoundException(id);
            });
    }
}
