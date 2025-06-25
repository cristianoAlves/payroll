package com.example.payroll.adapters.inbound.rest.dto;

public interface PayrollRequest {

    default boolean isA(Class<?> aClass) {
        return aClass.isAssignableFrom(getClass());
    }
}
