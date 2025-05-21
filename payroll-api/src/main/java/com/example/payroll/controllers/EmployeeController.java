package com.example.payroll.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.payroll.domain.entity.Employee;
import com.example.payroll.domain.service.employee.EmployeeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    EntityModel<Employee> newEmployee(@RequestBody Employee newEmployee) {

        Employee employee = employeeService.saveEmployee(newEmployee);
        return createEntityModel(employee);
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable("id") Long id) {
        Employee employee = employeeService.getById(id);

        return createEntityModel(employee);
    }

    private EntityModel<Employee> createEntityModel(Employee employee) {
        return EntityModel.of(employee,
            linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
            linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
    }
}
