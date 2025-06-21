package com.example.payroll.domain.employee.exception;

public class PayrollGenericException extends RuntimeException {

    public PayrollGenericException(String error, String message) {
        super(String.format("Unexpected error: Error: [%s]. Message: %s", error, message));
    }
}
