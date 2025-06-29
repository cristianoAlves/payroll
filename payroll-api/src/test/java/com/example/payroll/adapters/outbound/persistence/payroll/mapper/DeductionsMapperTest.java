package com.example.payroll.adapters.outbound.persistence.payroll.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.payroll.adapters.outbound.persistence.BaseMapperTest;
import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.DeductionsEntity;
import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.InssTaxEntity;
import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.TaxEntity;
import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.WithholdingRateEntity;
import com.example.payroll.domain.payroll.model.Deductions;
import com.example.payroll.domain.payroll.model.Tax;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DeductionsMapperTest extends BaseMapperTest {

    @Autowired
    private DeductionsMapper mapper;

    @Test
    public void fromEntity() {
        DeductionsEntity deductionsEntity = createDeductionEntity();
        Deductions deductions = mapper.fromEntity(deductionsEntity);
        validateDeductions(deductions, deductionsEntity);
    }
}