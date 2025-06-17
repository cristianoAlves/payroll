package com.example.payroll.adapters.outbound.persistence.employee.repository;

import com.example.payroll.adapters.outbound.persistence.employee.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepositoryJpa extends JpaRepository<EmployeeEntity, Long> {

}
