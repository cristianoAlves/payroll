package com.example.payroll.adapters.outbound.persistence.payroll.mapper;

import com.example.payroll.adapters.outbound.persistence.BaseMapperTest;
import com.example.payroll.adapters.outbound.persistence.payroll.entity.PayrollEntity;
import com.example.payroll.domain.payroll.model.Payroll;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

class PayrollMapperTest extends BaseMapperTest {

    @Autowired
    private PayrollMapper mapper;

    @Test
    public void fromEntity() {
        PayrollEntity payrollEntity = createPayrollEntity();
        Payroll payroll = mapper.fromEntity(payrollEntity);
        validatePayroll(payroll, payrollEntity);
    }

    private void validatePayroll(Payroll payroll, PayrollEntity payrollEntity) {
        assertThat(payroll.id()).isEqualTo(payrollEntity.getId());
        assertThat(payroll.grossSalary()).isEqualTo(payrollEntity.getGrossSalary());
        assertThat(payroll.netSalary()).isEqualTo(payrollEntity.getNetSalary());
        assertThat(payroll.period()).isEqualTo(payrollEntity.getPeriod());
        validateEmployee(payroll.employee(), payrollEntity.getEmployee());
        validateDeductions(payroll.deductions(), payrollEntity.getDeductions());
    }

    private PayrollEntity createPayrollEntity() {
        return PayrollEntity.builder()
            .deductions(createDeductionEntity())
            .netSalary(BigDecimal.ONE)
            .grossSalary(BigDecimal.ONE)
            .period(LocalDate.now())
            .employee(createEmployeeEntity())
            .id(10L)
            .build();
    }
}