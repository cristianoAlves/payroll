package com.example.payroll.domain.payroll.model;

import java.util.Collection;

public record Deductions(
    Long id,
    Collection<Tax> taxes
) {

}
