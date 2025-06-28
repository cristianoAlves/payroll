package com.example.payroll.adapters.outbound.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.payroll.BaseTest;
import com.example.payroll.adapters.outbound.persistence.employee.entity.BankAccountEntity;
import com.example.payroll.adapters.outbound.persistence.employee.entity.EmployeeEntity;
import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.DeductionsEntity;
import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.InssTaxEntity;
import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.TaxEntity;
import com.example.payroll.adapters.outbound.persistence.payroll.entity.tax.WithholdingRateEntity;
import com.example.payroll.domain.employee.model.Employee;
import com.example.payroll.domain.payroll.model.Deductions;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MapperTestConfig.class)
public class BaseMapperTest extends BaseTest {

    protected void validateDeductions(Deductions deductions, DeductionsEntity deductionsEntity) {
        assertThat(deductions.id()).isEqualTo(deductionsEntity.getId());
        assertThat(deductions.taxes()).hasSameSizeAs(deductionsEntity.getDeductionTax());
    }

    protected void validateEmployee(Employee employee, EmployeeEntity entity) {
        assertThat(employee.contracts()).hasSameSizeAs(entity.getContracts());
        assertThat(employee.id()).isEqualTo(entity.getId());
        assertThat(employee.name()).isEqualTo(entity.getName());
        assertThat(employee.cpf()).isEqualTo(entity.getCpf());
        assertThat(employee.bankAccount().account()).isEqualTo(entity.getBankAccount().getAccount());
        assertThat(employee.bankAccount().branch()).isEqualTo(entity.getBankAccount().getBranch());
    }

    protected DeductionsEntity createDeductionEntity() {
        return DeductionsEntity.builder()
            .deductionTax(List.of(createTaxEntity()))
            .id(10L)
            .build();
    }

    protected EmployeeEntity createEmployeeEntity() {
        return EmployeeEntity.builder()
            .bankAccount(BankAccountEntity.builder()
                .id(10L)
                .account("100")
                .branch("1")
                .build())
            .cpf("1234456")
            .contracts(List.of(createContractEntity(START_DATE, END_DATE)))
            .name("name")
            .build();
    }

    protected TaxEntity createTaxEntity() {
        return InssTaxEntity.builder()
            .withholdings(List.of(createWithHoldings()))
            .build();
    }

    protected WithholdingRateEntity createWithHoldings() {
        return WithholdingRateEntity.builder()
            .id(10L)
            .max(new BigDecimal(10))
            .min(new BigDecimal(12))
            .rate(new BigDecimal("0.1"))
            .build();
    }
}
