package com.example.payroll.adapters.inbound.rest.validators.Employee;

import com.example.payroll.adapters.inbound.rest.dto.EmployeeRequest;
import com.example.payroll.adapters.inbound.rest.dto.PayrollRequest;
import com.example.payroll.adapters.inbound.rest.validators.PayrollValidatorStrategy;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.contract.port.in.ContractValidationUseCase;
import com.example.payroll.domain.employee.exception.ContractHasOverlapException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeValidatorDtoContracts implements PayrollValidatorStrategy<EmployeeRequest> {

    private final ContractValidationUseCase contractValidationUseCase;

    @Override
    public boolean accepts(PayrollRequest dto) {
        return dto.isA(EmployeeRequest.class);
    }

    @Override
    public void validate(EmployeeRequest dto) {
        log.info("Starting validation for EmployeeRequest Contracts [{}]", dto);
        doValidate(dto.contracts());
    }

    private void doValidate(Collection<Contract> contracts) {
        Map<Contract, List<Contract>> overlappedRequestContracts = contractValidationUseCase.overlap(contracts);

        if (!overlappedRequestContracts.isEmpty()) {
            log.error("Some of the given contracts overlap with each other. Overlaps: [{}]", overlappedRequestContracts);
            throw new ContractHasOverlapException(overlappedRequestContracts, "Some of the given contracts overlap with each other");
        }
    }
}
