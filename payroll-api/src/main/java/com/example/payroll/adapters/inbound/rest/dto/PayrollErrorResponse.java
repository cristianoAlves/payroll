package com.example.payroll.adapters.inbound.rest.dto;

import com.example.payroll.domain.contract.model.Contract;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record PayrollErrorResponse(
    String message,
    LocalDateTime createdAt,
    Map<String, List<Contract>> metadata
) {

}
