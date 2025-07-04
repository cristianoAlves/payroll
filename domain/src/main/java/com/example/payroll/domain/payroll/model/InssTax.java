package com.example.payroll.domain.payroll.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class InssTax implements Tax {

    private final Long id;
    private final String name;
    private final List<WithholdingRate> withholdings;
    private final LocalDate validFrom;
    private final LocalDate validTo;

    public InssTax(Long id, String name, List<WithholdingRate> withholdings, LocalDate validFrom, LocalDate validTo) {
        this.id = id;
        this.name = name;
        this.withholdings = withholdings;
        this.validFrom = validFrom;
        this.validTo = validTo;
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

    public List<WithholdingRate> getWithholdings() {
        return withholdings;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }
}
