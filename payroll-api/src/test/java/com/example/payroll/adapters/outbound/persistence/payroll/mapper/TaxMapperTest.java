package com.example.payroll.adapters.outbound.persistence.payroll.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.payroll.adapters.outbound.persistence.BaseMapperTest;
import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.TaxEntity;
import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.WithholdingRateEntity;
import com.example.payroll.domain.payroll.model.Tax;
import com.example.payroll.domain.payroll.model.WithholdingRate;
import java.util.List;
import org.junit.jupiter.api.Test;

class TaxMapperTest extends BaseMapperTest {

    TaxMapper mapper = getMapper(TaxMapper.class);

    @Test
    public void fromEntity() {
        TaxEntity taxEntity = createTaxEntity();
        Tax tax = mapper.fromEntity(taxEntity);
        validateTax(tax, taxEntity);
    }

    private void validateTax(Tax tax, TaxEntity taxEntity) {
        assertThat(tax.id()).isEqualTo(taxEntity.getId());
        assertThat(tax.name()).isEqualTo(taxEntity.getName());
    }

    @Test
    public void toDomainWithholdings() {
        List<WithholdingRateEntity> withHoldings = List.of(createWithHoldings());
        List<WithholdingRate> domainWithholdings = mapper.toDomainWithholdings(withHoldings);
        validateWithHoldings(domainWithholdings, withHoldings);
    }

    private void validateWithHoldings(List<WithholdingRate> domainWithholdings, List<WithholdingRateEntity> withHoldings) {
        assertThat(domainWithholdings).hasSameSizeAs(withHoldings);
        validateWithHolding(domainWithholdings.get(0), withHoldings.get(0));
    }

    private void validateWithHolding(WithholdingRate withholdingRate, WithholdingRateEntity withholdingRateEntity) {
        assertThat(withholdingRate.id()).isEqualTo(withholdingRateEntity.getId());
        assertThat(withholdingRate.max()).isEqualTo(withholdingRateEntity.getMax());
        assertThat(withholdingRate.rate()).isEqualTo(withholdingRateEntity.getRate());
    }
}