package com.example.payroll.domain.contract.model;

import com.example.payroll.domain.employee.model.Employee;
import java.math.BigDecimal;
import java.time.LocalDate;

public record Contract(
    Long id,
    BigDecimal salary,
    LocalDate startDate,
    LocalDate endDate,
    boolean active,
    Employee employee) {

    @SuppressWarnings("checkstyle:hiddenfield")
    public Contract addEmployee(Employee employee) {
        return new Contract(this.id, this.salary, this.startDate, this.endDate, this.active, employee);
    }
}
