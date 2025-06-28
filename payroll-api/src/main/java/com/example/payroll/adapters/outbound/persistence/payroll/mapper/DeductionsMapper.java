package com.example.payroll.adapters.outbound.persistence.payroll.mapper;

import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.DeductionsEntity;
import com.example.payroll.config.MapperConfiguration;
import com.example.payroll.domain.payroll.model.Deductions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class, uses = TaxMapper.class)
public abstract class DeductionsMapper {

    @Mapping(target = "id", source = "deductionsEntity.id")
    @Mapping(target = "taxes", source = "deductionTax")
    protected abstract Deductions fromEntity(DeductionsEntity deductionsEntity);
}
