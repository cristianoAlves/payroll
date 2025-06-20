package com.example.payroll.adapters.inbound.rest;

import com.example.payroll.adapters.inbound.rest.dto.ContractRequest;
import com.example.payroll.adapters.outbound.persistence.contract.mapper.ContractMapper;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.contract.port.in.ContractUseCase;
import com.example.payroll.domain.employee.model.Employee;
import com.example.payroll.domain.employee.port.in.EmployeeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ContractController {

    private final ContractUseCase contractUseCase;
    private final EmployeeUseCase employeeUseCase;
    private final ContractMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<Contract> newContract(@RequestBody ContractRequest request) {
        Employee employee = employeeUseCase.getById(request.employeeId());
        Contract contract = mapper.fromRequest(request);
        return EntityModel.of(contractUseCase.assignContractToEmployee(contract, employee));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    EntityModel<Contract> one(@PathVariable("id") Long id) {
        return EntityModel.of(contractUseCase.findOne(id));
    }

    //    private EntityModel<Contract> createEntityModel(Contract contract) {
    //        return EntityModel.of(contract,
    //            linkTo(methodOn(ContractController.class).one(contract.id())).withSelfRel(),
    //            linkTo(methodOn(ContractController.class).all()).withRel("contracts"));
    //    }
}
