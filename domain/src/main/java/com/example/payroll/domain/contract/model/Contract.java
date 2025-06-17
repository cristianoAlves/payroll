package com.example.payroll.domain.contract.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Contract(
    Long id,
    BigDecimal salary,
    LocalDate startDate,
    LocalDate endDate,
    boolean active) {

}
