package com.example.payroll.adapters.inbound.rest.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.payroll.adapters.inbound.rest.dto.ContractRequest;
import com.example.payroll.adapters.inbound.rest.dto.EmployeeRequest;
import com.example.payroll.adapters.inbound.rest.validators.ValidationFactory;
import com.example.payroll.adapters.outbound.persistence.employee.mapper.EmployeeMapper;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.employee.model.BankAccount;
import com.example.payroll.domain.employee.model.Employee;
import com.example.payroll.domain.employee.port.in.EmployeeUseCase;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeUseCase employeeUseCase;
    private final EmployeeMapper mapper;
    private final ValidationFactory validationFactory;

    @GetMapping
    CollectionModel<EntityModel<Employee>> all() {

        List<EntityModel<Employee>> employees = employeeUseCase.getAllEmployees().stream()
            .map(this::createEntityModel)
            .toList();

        return CollectionModel.of(employees,
            linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<Employee> newEmployee(@RequestBody EmployeeRequest newEmployee) {
        validationFactory.getValidators(newEmployee)
            .forEach(v -> v.validate(newEmployee));
        return createEntityModel(employeeUseCase.saveEmployee(mapper.fromRequest(newEmployee)));
    }

    @PatchMapping("/{id}/bank-account")
    @ResponseStatus(HttpStatus.OK)
    EntityModel<Employee> assignBankAccount(@PathVariable("id") Long id, @RequestBody BankAccount bankAccount) {
        return createEntityModel(employeeUseCase.assignBankAccount(bankAccount, id));
    }

    @PatchMapping("/{id}/contracts")
    @ResponseStatus(HttpStatus.OK)
    EntityModel<Employee> assignContracts(@PathVariable("id") Long id, @RequestBody Collection<Contract> contracts) {
        ContractRequest requestToBeValidated = new ContractRequest(id, contracts);
        validationFactory.getValidators(requestToBeValidated)
            .forEach(v -> v.validate(requestToBeValidated));
        return createEntityModel(employeeUseCase.assignContracts(contracts, id));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    EntityModel<Employee> one(@PathVariable("id") Long id) {
        return createEntityModel(employeeUseCase.getById(id));
    }

    private EntityModel<Employee> createEntityModel(Employee employee) {
        return EntityModel.of(employee,
            linkTo(methodOn(EmployeeController.class).one(employee.id())).withSelfRel());
    }
}