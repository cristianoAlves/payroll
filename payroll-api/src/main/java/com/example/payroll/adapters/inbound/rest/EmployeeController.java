package com.example.payroll.adapters.inbound.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.payroll.application.services.EmployeeService;
import com.example.payroll.domain.employee.model.Employee;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {

        List<EntityModel<Employee>> employees = employeeService.getAllEmployees().stream()
            .map(this::createEntityModel)
            .toList();

        return CollectionModel.of(employees,
            linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<Employee> newEmployee(@RequestBody Employee newEmployee) {
        return createEntityModel(employeeService.saveEmployee(newEmployee));
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable("id") Long id) {
        return createEntityModel(employeeService.getById(id));
    }

    private EntityModel<Employee> createEntityModel(Employee employee) {
        return EntityModel.of(employee,
            linkTo(methodOn(EmployeeController.class).one(employee.id())).withSelfRel(),
            linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
    }
}