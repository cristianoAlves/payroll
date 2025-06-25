package com.example.payroll.adapters.outbound.persistence.contract.mapper;

import com.example.payroll.adapters.outbound.persistence.contract.entity.ContractEntity;
import com.example.payroll.config.MapperConfiguration;
import com.example.payroll.domain.contract.model.Contract;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public abstract class ContractMapper {

    public abstract Contract fromEntity(ContractEntity entity);

    public abstract ContractEntity fromContract(Contract contract);
}
