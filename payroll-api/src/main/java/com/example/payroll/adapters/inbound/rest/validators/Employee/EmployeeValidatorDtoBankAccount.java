package com.example.payroll.adapters.inbound.rest.validators.Employee;

import com.example.payroll.adapters.inbound.rest.dto.EmployeeRequest;
import com.example.payroll.adapters.inbound.rest.dto.PayrollRequest;
import com.example.payroll.adapters.inbound.rest.validators.PayrollValidatorStrategy;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmployeeValidatorDtoBankAccount implements PayrollValidatorStrategy<EmployeeRequest> {

    @Override
    public boolean accepts(PayrollRequest dto) {
        return dto.isA(EmployeeRequest.class);
    }

    @Override
    public void validate(EmployeeRequest dto) {
        log.info("Starting validation for [{}]", dto.bankAccount());
        doValidate(dto);
    }

    private void doValidate(EmployeeRequest dto) {
        if (Objects.isNull(dto.bankAccount()) ||
            StringUtils.isEmpty(dto.bankAccount().account()) ||
            StringUtils.isEmpty(dto.bankAccount().branch())
        ) {
            log.error("Validation failed for EmployeeRequest.bankAccount [{}]", dto.bankAccount());
            throw new IllegalArgumentException("Bank Account cannot be null or empty");
        }
    }
}
