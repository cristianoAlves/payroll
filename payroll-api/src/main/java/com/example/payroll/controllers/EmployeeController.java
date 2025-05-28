package com.example.payroll.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.payroll.domain.entity.Employee;
import com.example.payroll.domain.service.employee.EmployeeService;
import com.example.payroll.dto.EmployeeDto;
import com.example.payroll.mappers.EmployeeMapper;
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
    private final EmployeeMapper mapper;

    @GetMapping("/employees")
    CollectionModel<EntityModel<EmployeeDto>> all() {

        List<EntityModel<EmployeeDto>> employees = employeeService.getAllEmployees().stream()
            .map(this::createEntityModel)
            .toList();

        return CollectionModel.of(employees,
            linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<EmployeeDto> newEmployee(@RequestBody Employee newEmployee) {

        Employee employee = employeeService.saveEmployee(newEmployee);
        return createEntityModel(employee);
    }

    @GetMapping("/employees/{id}")
    EntityModel<EmployeeDto> one(@PathVariable("id") Long id) {
        Employee employee = employeeService.getById(id);

        return createEntityModel(employee);
    }

    private EntityModel<EmployeeDto> createEntityModel(Employee employee) {
        EmployeeDto dto = mapper.from(employee);
        return EntityModel.of(dto,
            linkTo(methodOn(EmployeeController.class).one(dto.getId())).withSelfRel(),
            linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
    }
}