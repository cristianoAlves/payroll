package com.example.payroll.adapters.outbound.persistence.payroll.mapper;

import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.InssTaxEntity;
import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.TaxEntity;
import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.WithholdingRateEntity;
import com.example.payroll.config.MapperConfiguration;
import com.example.payroll.domain.payroll.model.InssTax;
import com.example.payroll.domain.payroll.model.Tax;
import com.example.payroll.domain.payroll.model.WithholdingRate;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public abstract class TaxMapper {

    public Tax fromEntity(TaxEntity taxEntity) {
        if (taxEntity instanceof InssTaxEntity inss) {
            return new InssTax(
                inss.getId(),
                inss.getName(),
                toDomainWithholdings(inss.getWithholdings())
            );
        }
        throw new IllegalArgumentException("Unknown tax type: " + taxEntity.getClass());
    }

    // Abstract method still processed by MapStruct
    protected abstract List<WithholdingRate> toDomainWithholdings(List<WithholdingRateEntity> entities);
}
