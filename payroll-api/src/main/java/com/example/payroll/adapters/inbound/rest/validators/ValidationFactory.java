package com.example.payroll.adapters.inbound.rest.validators;

import com.example.payroll.adapters.inbound.rest.dto.PayrollRequest;
import java.util.List;

public interface ValidationFactory {

    <T extends PayrollRequest> List<PayrollValidatorStrategy<T>> getValidators(T dto);
}
