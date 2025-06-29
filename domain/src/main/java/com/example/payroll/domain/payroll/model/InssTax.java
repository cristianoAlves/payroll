package com.example.payroll.domain.payroll.model;

import java.math.BigDecimal;
import java.util.List;

public class InssTax implements Tax {
    private final Long id;
    private final String name;
    private final List<WithholdingRate> withholdings;

    public InssTax(Long id, String name, List<WithholdingRate> withholdings) {
        this.id = id;
        this.name = name;
        this.withholdings = withholdings;
    }

    @Override
    public Long id() {
        return this.id;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public BigDecimal calculate(BigDecimal baseSalary) {
        return null;
    }
}
