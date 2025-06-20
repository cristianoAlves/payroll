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
        select ce from ContractEntity ce
        
        """)
    List<ContractEntity> findOverlappingContracts(@Param("employeeId") Long id,
        @Param("startDate") LocalDate startDate, @Param("endDate")LocalDate endDate);
}
