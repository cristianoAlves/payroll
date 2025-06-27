package com.example.payroll.domain.payroll.model;

import java.math.BigDecimal;

public record WithholdingRate(
    Long id,
    BigDecimal min,
    BigDecimal max,
    BigDecimal rate
) {

}
