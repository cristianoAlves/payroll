package com.example.payroll.adapters.inbound.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContractRequest(
    Long employeeId,
    BigDecimal salary,
    LocalDate startDate,
    LocalDate endDate,
    boolean active
) {

}
