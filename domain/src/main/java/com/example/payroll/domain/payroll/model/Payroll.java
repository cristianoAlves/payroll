package com.example.payroll.domain.payroll.model;

import com.example.payroll.domain.employee.model.Employee;
import java.math.BigDecimal;
import java.time.LocalDate;

public record Payroll(
    Long id,
    Employee employee,
    LocalDate period,
    BigDecimal salary) {

}
