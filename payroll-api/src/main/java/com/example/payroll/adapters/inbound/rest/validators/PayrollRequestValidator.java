package com.example.payroll.adapters.inbound.rest.validators;

import com.example.payroll.adapters.inbound.rest.dto.PayrollRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PayrollRequestValidator implements ValidationFactory {

    private final List<PayrollValidatorStrategy<? extends PayrollRequest>> validators;

    @Override
    public <T extends PayrollRequest> List<PayrollValidatorStrategy<T>> getValidators(T dto) {
        return validators.stream()
            .filter(v -> v.accepts(dto))
            .map(v -> (PayrollValidatorStrategy<T>) v)
            .toList();
    }
}
