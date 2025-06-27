package com.example.payroll.domain.payroll.model;

import java.math.BigDecimal;

public interface Tax {

    String name();

    BigDecimal calculate(BigDecimal baseSalary);
}
