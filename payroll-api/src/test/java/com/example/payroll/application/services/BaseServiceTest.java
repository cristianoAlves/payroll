package com.example.payroll.application.services;

import com.example.payroll.BaseTest;
import com.example.payroll.domain.contract.model.Contract;
import com.example.payroll.domain.employee.model.Employee;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class BaseServiceTest extends BaseTest {

    protected Contract createContract(Long id, LocalDate startDate, LocalDate endDate) {
        return new Contract(id, new BigDecimal(1000), startDate, endDate, true);
    }

    protected Employee createEmployee(long id, String name, Contract contract) {
        return new Employee(id, name, "cpf123", null, List.of(contract));
    }

}
