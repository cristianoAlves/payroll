package com.example.payroll.adapters.outbound.persistence.payroll.mapper;

import com.example.payroll.adapters.outbound.persistence.payroll.entity.PayrollEntity;
import com.example.payroll.config.MapperConfiguration;
import com.example.payroll.domain.payroll.model.Payroll;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class, uses = DeductionsMapper.class)
public abstract class PayrollMapper {

    @Mapping(target = "employee.assignBankAccount", ignore = true)
    @Mapping(target = "employee.assignContracts", ignore = true)
    public abstract Payroll fromEntity(PayrollEntity payrollEntity);

}
