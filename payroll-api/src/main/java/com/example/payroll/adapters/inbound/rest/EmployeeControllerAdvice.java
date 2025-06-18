package com.example.payroll.adapters.inbound.rest;

import com.example.payroll.domain.employee.exception.EmployeeNotFoundException;
import com.example.payroll.domain.employee.exception.EmployeeWithSameCpfException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
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
}
