package com.example.payroll.adapters.outbound.persistence.employee.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.payroll.adapters.inbound.rest.dto.EmployeeRequest;
import com.example.payroll.adapters.outbound.persistence.BaseMapperTest;
import com.example.payroll.adapters.outbound.persistence.employee.entity.BankAccountEntity;
import com.example.payroll.adapters.outbound.persistence.employee.entity.EmployeeEntity;
import com.example.payroll.domain.employee.model.Employee;
import java.util.List;
import org.junit.jupiter.api.Test;

class EmployeeMapperTest extends BaseMapperTest {

    private final EmployeeMapper mapper = getMapper(EmployeeMapper.class);

    @Test
    public void fromEntity() {
        EmployeeEntity entity = createEmployeeEntity();
        Employee employee = mapper.fromEntity(entity);
        validateEmployee(employee, entity);
    }

    @Test
    public void fromEntityNull() {
        assertThat(mapper.fromEntity(null)).isNull();
    }

    @Test
    public void fromRequest() {
        EmployeeRequest request = createEmployeeRequest();
        Employee employee = mapper.fromRequest(request);
        validateEmployeeRequest(employee, request);
    }

    @Test
    public void fromRequestNull() {
        assertThat(mapper.fromRequest(null)).isNull();
    }

    @Test
    public void fromEmployee() {
        Employee employee = createEmployeeFull();
        EmployeeEntity entity = mapper.fromEmployee(employee);
        validateEmployee(employee, entity);
    }

    @Test
    public void fromEmployeeNull() {
        assertThat(mapper.fromEmployee(null)).isNull();
    }

    private void validateEmployeeRequest(Employee employee, EmployeeRequest request) {
        assertThat(employee.name()).isEqualTo(request.name());
        assertThat(employee.cpf()).isEqualTo(request.cpf());
        assertThat(employee.bankAccount().account()).isEqualTo(request.bankAccount().account());
        assertThat(employee.bankAccount().branch()).isEqualTo(request.bankAccount().branch());
        assertThat(employee.contracts()).hasSameSizeAs(request.contracts());
    }

    private EmployeeRequest createEmployeeRequest() {
        return new EmployeeRequest(10L, "name", "cpf", createBankAccount(), List.of(createContract(START_DATE, END_DATE)));
    }
}