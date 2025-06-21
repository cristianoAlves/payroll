package com.example.payroll.adapters.outbound.persistence.contract.repository;

import com.example.payroll.adapters.outbound.persistence.contract.entity.ContractEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepositoryJpa extends JpaRepository<ContractEntity, Long> {

    @Query("""
        SELECT c FROM ContractEntity c
        LEFT JOIN EmployeeEntity e on  (e.id = c.employeeId)
        WHERE e.id = :employeeId AND
              c.active = true AND
              (:startDate <= c.endDate OR c.endDate IS NULL) AND
              (:endDate >= c.startDate OR c.startDate IS NULL)
        """)
    List<ContractEntity> findOverlappingContracts(@Param("employeeId") Long employeeId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
