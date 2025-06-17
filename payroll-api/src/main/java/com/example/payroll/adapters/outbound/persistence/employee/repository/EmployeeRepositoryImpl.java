package com.example.payroll.adapters.outbound.persistence.employee.repository;

import com.example.payroll.adapters.outbound.persistence.employee.entity.EmployeeEntity;
import com.example.payroll.adapters.outbound.persistence.employee.mapper.EmployeeMapper;
import com.example.payroll.domain.employee.model.Employee;
import com.example.payroll.domain.employee.port.out.EmployeeRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final EmployeeRepositoryJpa employeeRepositoryJpa;
    private final EmployeeMapper employeeMapper;

    @Override
    public Employee save(Employee employee) {
        return employeeMapper.fromEntity(employeeRepositoryJpa.save(employeeMapper.fromEmployee(employee)));
    }

    @Override
    public Collection<Employee> findAll() {
        List<EmployeeEntity> all = employeeRepositoryJpa.findAll();
        return all.stream()
            .map(employeeMapper::fromEntity)
            .toList();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepositoryJpa.findById(id)
            .map(employeeMapper::fromEntity);
    }
}
