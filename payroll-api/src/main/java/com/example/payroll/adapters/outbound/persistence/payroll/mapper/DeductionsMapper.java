package com.example.payroll.adapters.outbound.persistence.payroll.mapper;

import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.DeductionsEntity;
import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.TaxEntity;
import com.example.payroll.config.MapperConfiguration;
import com.example.payroll.domain.payroll.model.Deductions;
import com.example.payroll.domain.payroll.model.Tax;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class, uses = TaxMapper.class)
public abstract class DeductionsMapper {

    public Deductions fromEntity(DeductionsEntity deductionsEntity) {
        return new Deductions(deductionsEntity.getId(), mapToTax(deductionsEntity.getDeductionTax()));
    }

    protected abstract Collection<Tax> mapToTax(List<TaxEntity> deductionTax);
}
