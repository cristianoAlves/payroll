package com.example.payroll.adapters.inbound.rest.controllers;

import com.example.payroll.adapters.inbound.rest.dto.PayrollErrorResponse;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.employee.exception.ContractHasOverlapException;
import com.example.payroll.domain.employee.exception.EmployeeNotFoundException;
import com.example.payroll.domain.employee.exception.EmployeeWithSameCpfException;
import com.example.payroll.domain.employee.exception.PayrollGenericException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    PayrollErrorResponse employeeNotFoundHandler(EmployeeNotFoundException ex) {
        log.error("employeeNotFound: {}", ex.getMessage());
        return createPayrollResponse(ex, Collections.emptyMap());
    }

    @ExceptionHandler(EmployeeWithSameCpfException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    PayrollErrorResponse employeeWithSameCpfExceptionHandler(EmployeeWithSameCpfException ex) {
        log.error("EmployeeWithSameCpfException: {}", ex.getMessage());
        return createPayrollResponse(ex, Collections.emptyMap());
    }

    @ExceptionHandler(PayrollGenericException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    PayrollErrorResponse genericExceptionHandler(PayrollGenericException ex) {
        log.error("Payroll Generic Exception: {}", ex.getMessage());
        return createPayrollResponse(ex, Collections.emptyMap());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    PayrollErrorResponse illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        log.error("Payroll IllegalArgumentException: {}", ex.getMessage());
        return createPayrollResponse(ex, Collections.emptyMap());
    }

    @ExceptionHandler(ContractHasOverlapException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    PayrollErrorResponse contractHasOverlapExceptionHandler(ContractHasOverlapException ex) {
        log.error("Payroll ContractHasOverlapException: {}", ex.getMessage());
        return createPayrollResponse(ex, ex.getContracts());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    PayrollErrorResponse notReadableExceptionHandler(HttpMessageNotReadableException ex) {
        log.error("Required request body is missing: {}", ex.getMessage());
        return createPayrollResponse(ex, Map.of());
    }

    private <T extends RuntimeException> PayrollErrorResponse createPayrollResponse(T ex, Map<String, List<Contract>> metadata) {
        return new PayrollErrorResponse(ex.getMessage(), LocalDateTime.now(), metadata);
    }
}
