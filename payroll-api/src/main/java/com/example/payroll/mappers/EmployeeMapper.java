package com.example.payroll.mappers;

import com.example.payroll.config.MapperConfiguration;
import com.example.payroll.domain.entity.Employee;
import com.example.payroll.dto.EmployeeDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public abstract class EmployeeMapper {

    public abstract EmployeeDto from(Employee employee);
}
