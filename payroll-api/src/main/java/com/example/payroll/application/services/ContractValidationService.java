package com.example.payroll.application.services;

import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.contract.port.in.ContractValidationUseCase;
import com.example.payroll.domain.contract.port.out.ContractRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContractValidationService implements ContractValidationUseCase {

    private final ContractRepository repository;

    @Override
    public Map<String, List<Contract>> overlap(Collection<Contract> contractsToBeVerified, Long employeeId) {
        log.debug("Testing for overlapping contracts for employee [{}]", employeeId);
        Map<String, List<Contract>> overlappedResult = new HashMap<>();

        contractsToBeVerified.forEach(subjectContract -> {
            log.debug("Searching for overlapping contracts with [{}]", subjectContract);
            List<Contract> overlappedFound = repository.findOverlappingContracts(employeeId, subjectContract.startDate(), subjectContract.endDate());
            if (!overlappedFound.isEmpty()) {
                log.debug("Found an overlapping contract with [{}]", subjectContract);
                overlappedResult.put(subjectContract.toString(), overlappedFound);
            }
        });
        log.debug("Returning as overlapped [{}]", overlappedResult);
        return overlappedResult;
    }

    @Override
    public Map<String, List<Contract>> overlap(Collection<Contract> contracts) {
        log.debug("Starting comparison of overlapping contracts [{}].", contracts);
        List<Contract> givenContracts = new ArrayList<>(contracts);
        Map<String, List<Contract>> resultOverlapped = new HashMap<>();

        for (int i = 0; i < givenContracts.size(); i++) {
            Contract contractA = givenContracts.get(i);
            for (int j = i + 1; j < givenContracts.size(); j++) {
                Contract contractB = givenContracts.get(j);
                log.debug("Comparing contractA [{}] with contractB [{}]", contractA, contractB);
                if (isOverlapped(contractA, contractB)) {
                    log.debug("ContractA overlaps with Contract B");
                    resultOverlapped.computeIfAbsent(contractA.toString(), v -> new ArrayList<>()).add(contractB);
                    resultOverlapped.computeIfAbsent(contractB.toString(), v -> new ArrayList<>()).add(contractA);
                }
            }
        }
        log.debug("Returning resultOverlapped [{}]", resultOverlapped);
        return resultOverlapped;
    }

    private boolean isOverlapped(Contract contractA, Contract contractB) {
        boolean startsBeforeOtherEnds = contractA.startDate() == null || contractB.endDate() == null || !contractA.startDate().isAfter(contractB.endDate());
        boolean endsAfterOtherStarts = contractA.endDate() == null || contractB.startDate() == null || !contractA.endDate().isBefore(contractB.startDate());

        return startsBeforeOtherEnds && endsAfterOtherStarts;
    }

    @Override
    public boolean isExpired(Contract contract) {
        return false;
    }
}
