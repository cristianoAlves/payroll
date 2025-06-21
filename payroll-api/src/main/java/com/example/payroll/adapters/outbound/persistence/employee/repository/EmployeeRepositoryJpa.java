package com.example.payroll.adapters.outbound.persistence.employee.repository;

import com.example.payroll.adapters.outbound.persistence.employee.entity.EmployeeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepositoryJpa extends JpaRepository<EmployeeEntity, Long> {

    @Query("SELECT e FROM EmployeeEntity e LEFT JOIN FETCH e.contracts")
    List<EmployeeEntity> findAllWithContracts();

    @Query("""
            SELECT e FROM EmployeeEntity e
            LEFT JOIN FETCH e.contracts
            WHERE e.id = :id
        """)
    Optional<EmployeeEntity> findByIdWithContracts(@Param("id") Long id);

}
