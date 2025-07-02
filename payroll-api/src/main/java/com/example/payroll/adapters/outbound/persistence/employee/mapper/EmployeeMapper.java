package com.example.payroll.adapters.outbound.persistence.employee.mapper;

import com.example.payroll.adapters.inbound.rest.dto.EmployeeRequest;
import com.example.payroll.adapters.outbound.persistence.contract.entity.ContractEntity;
import com.example.payroll.adapters.outbound.persistence.employee.entity.EmployeeEntity;
import com.example.payroll.config.MapperConfiguration;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.employee.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public abstract class EmployeeMapper {

    @Mapping(target = "assignBankAccount", ignore = true)
    @Mapping(target = "assignContracts", ignore = true)
    public abstract Employee fromEntity(EmployeeEntity employeeEntity);

    @Mapping(target = "assignBankAccount", ignore = true)
    @Mapping(target = "assignContracts", ignore = true)
    public abstract Employee fromRequest(EmployeeRequest request);

    @Mapping(target = "bankAccount.id", ignore = true)
    public abstract EmployeeEntity fromEmployee(Employee employee);

    @Mapping(target = "employeeId", ignore = true)
    public abstract ContractEntity toContractEntity(Contract contract);
}
