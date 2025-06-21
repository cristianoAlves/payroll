package com.example.payroll.adapters.inbound.rest.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.contract.port.in.ContractUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ContractController {

    private final ContractUseCase contractUseCase;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    EntityModel<Contract> one(@PathVariable("id") Long id) {
        return createEntityModel(contractUseCase.findOne(id));
    }

    private EntityModel<Contract> createEntityModel(Contract contract) {
        return EntityModel.of(contract,
            linkTo(methodOn(ContractController.class).one(contract.id())).withSelfRel());
    }
}
