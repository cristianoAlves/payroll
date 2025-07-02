package com.example.payroll.adapters.outbound.persistence.contract.mapper;

import com.example.payroll.adapters.outbound.persistence.contract.entity.ContractEntity;
import com.example.payroll.config.MapperConfiguration;
import com.example.payroll.domain.contract.model.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public abstract class ContractMapper {

    public abstract Contract fromEntity(ContractEntity entity);

    @Mapping(target = "employeeId", ignore = true)
    public abstract ContractEntity fromContract(Contract contract);
}
