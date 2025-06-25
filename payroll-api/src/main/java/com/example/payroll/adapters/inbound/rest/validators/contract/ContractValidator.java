package com.example.payroll.adapters.inbound.rest.validators.contract;

import com.example.payroll.adapters.inbound.rest.dto.ContractRequest;
import com.example.payroll.adapters.inbound.rest.dto.PayrollRequest;
import com.example.payroll.adapters.inbound.rest.validators.PayrollValidatorStrategy;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.contract.port.in.ContractValidationUseCase;
import com.example.payroll.domain.employee.exception.ContractHasOverlapException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContractValidator implements PayrollValidatorStrategy<ContractRequest> {

    private final ContractValidationUseCase contractValidationUseCase;

    @Override
    public boolean accepts(PayrollRequest dto) {
        return dto.isA(ContractRequest.class);
    }

    @Override
    public void validate(ContractRequest dto) {
        doValidateContracts(dto);
        doValidateRequestContractsWhichOverlapEachOthers(dto.contracts());
        doValidateRequestContractsWhichOverlapWithSavedContracts(dto);
    }

    private void doValidateContracts(ContractRequest dto) {
        if (Objects.isNull(dto) || Objects.isNull(dto.contracts()) || dto.contracts().isEmpty()) {
            log.error("Validation failed for EmployeeRequest.contracts [{}]", dto);
            throw new IllegalArgumentException("At least one contract is required.");
        }
    }

    private void doValidateRequestContractsWhichOverlapWithSavedContracts(ContractRequest dto) {
        log.info("Validating request contracts for overlaps with saved contracts");
        Map<String, List<Contract>> overlap = contractValidationUseCase.overlap(dto.contracts(), dto.employeeId());

        if (!overlap.isEmpty()) {
            log.error("Some contracts overlap with others already saved for this employee. Overlaps: [{}]", overlap);
            throw new ContractHasOverlapException(overlap, "Some of these contracts overlap with already saved ones.");
        }
    }

    private void doValidateRequestContractsWhichOverlapEachOthers(Collection<Contract> contracts) {
        log.info("Validating request contracts for overlaps");
        Map<String, List<Contract>> overlappedRequestContracts = contractValidationUseCase.overlap(contracts);

        if (!overlappedRequestContracts.isEmpty()) {
            log.error("Some of the given contracts overlap with each other. Overlaps: [{}]", overlappedRequestContracts);
            throw new ContractHasOverlapException(overlappedRequestContracts, "Some of the given contracts overlap with each other");
        }
    }
}
