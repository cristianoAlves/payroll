package com.example.payroll;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.example.payroll.adapters.outbound.persistence.contract.entity.ContractEntity;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.employee.model.BankAccount;
import com.example.payroll.domain.employee.model.Employee;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.mapstruct.factory.Mappers;

public abstract class BaseTest {
    protected static final LocalDate START_DATE = LocalDate.now();
    protected static final LocalDate END_DATE = START_DATE.plusDays(10);

    protected <T> T getMapper(Class<T> mapper) {
        return Mappers.getMapper(mapper);
    }

    protected Employee createEmployeeFull() {
        return new Employee(null, "name", "cpf-" + RandomUtil.getPositiveInt(),
            createBankAccount(), List.of(createContract(START_DATE, END_DATE))
        );
    }

    protected BankAccount createBankAccount() {
        return new BankAccount("account", "branch");
    }

    protected Contract createContract(LocalDate startDate, LocalDate endDate) {
        return new Contract(null, new BigDecimal(1000), startDate, endDate, true);
    }

    protected ContractEntity createContractEntity(LocalDate startDate, LocalDate endDate) {
        return ContractEntity.builder()
            .employeeId(10L)
            .salary(new BigDecimal(10))
            .startDate(startDate)
            .endDate(endDate)
            .active(true)
            .build();
    }

}
