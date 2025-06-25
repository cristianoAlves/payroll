package com.example.payroll.adapters.inbound.rest.validators;

import com.example.payroll.adapters.inbound.rest.dto.PayrollRequest;

public interface PayrollValidatorStrategy<T extends PayrollRequest> {

    boolean accepts(PayrollRequest dto);

    void validate(T dto);
}
