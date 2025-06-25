package com.example.payroll.adapters.inbound.rest.validators.Employee;

import com.example.payroll.adapters.inbound.rest.dto.EmployeeRequest;
import com.example.payroll.adapters.inbound.rest.dto.PayrollRequest;
import com.example.payroll.adapters.inbound.rest.validators.PayrollValidatorStrategy;
import com.example.payroll.domain.contract.model.Contract;
import java.util.Collection;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Order(10)
@SuppressWarnings("checkstyle:MagicNumber")
public class EmployeeValidatorDtoAttributes implements PayrollValidatorStrategy<EmployeeRequest> {

    @Override
    public boolean accepts(PayrollRequest dto) {
        return dto.isA(EmployeeRequest.class);
    }

    @Override
    public void validate(EmployeeRequest dto) {
        log.info("Starting validating attributes [{}]", dto);
        doValidateDto(dto);
        doValidate(dto.name(), "Validation failed for EmployeeRequest.name [{}]", "Name cannot be null or empty");
        doValidate(dto.cpf(), "Validation failed for EmployeeRequest.cpf [{}]", "CPF cannot be null or empty");
        doValidateContracts(dto.contracts());
    }

    private void doValidateContracts(Collection<Contract> contracts) {
        if (Objects.isNull(contracts) || contracts.isEmpty()) {
            log.error("Validation failed for EmployeeRequest.contracts [{}]", contracts);
            throw new IllegalArgumentException("At least one contract is required.");
        }
    }

    private void doValidateDto(EmployeeRequest dto) {
        if (Objects.isNull(dto)) {
            log.error("Validation failed for EmployeeRequest. Request is null");
            throw new IllegalArgumentException("EmployeeRequest cannot be null or empty");
        }
    }

    private void doValidate(String attribute, String logMessage, String exceptionMessage) {
        if (StringUtils.isEmpty(attribute)) {
            log.error(logMessage, attribute);
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
}
