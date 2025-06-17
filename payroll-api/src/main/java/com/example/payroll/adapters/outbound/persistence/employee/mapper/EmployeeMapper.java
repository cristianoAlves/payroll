package com.example.payroll.adapters.outbound.persistence.employee.mapper;

import com.example.payroll.config.MapperConfiguration;
import com.example.payroll.domain.employee.model.Employee;
import com.example.payroll.adapters.outbound.persistence.employee.entity.EmployeeEntity;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public abstract class EmployeeMapper {

    public abstract Employee fromEntity(EmployeeEntity employeeEntity);

    public abstract EmployeeEntity fromEmployee(Employee employee);
}
