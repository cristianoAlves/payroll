package com.example.payroll.adapters.inbound.rest.controllers;

import com.example.payroll.domain.employee.exception.ContractHasOverlapException;
import com.example.payroll.domain.employee.exception.EmployeeNotFoundException;
import com.example.payroll.domain.employee.exception.EmployeeWithSameCpfException;
import com.example.payroll.domain.employee.exception.PayrollGenericException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {EmployeeController.class})
@Slf4j
public class EmployeeControllerAdvice {

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(EmployeeNotFoundException ex) {
        log.error("employeeNotFound: {}", ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(EmployeeWithSameCpfException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String employeeWithSameCpfExceptionHandler(EmployeeWithSameCpfException ex) {
        log.error("EmployeeWithSameCpfException: {}", ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(PayrollGenericException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String genericExceptionHandler(PayrollGenericException ex) {
        log.error("Payroll Generic Exception: {}", ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        log.error("Payroll IllegalArgumentException: {}", ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(ContractHasOverlapException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String contractHasOverlapExceptionHandler(ContractHasOverlapException ex) {
        log.error("Payroll ContractHasOverlapException: {}", ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String notReadableExceptionHandler(HttpMessageNotReadableException ex) {
        log.error("Required request body is missing: {}", ex.getMessage());
        return ex.getMessage();
    }
}
