package com.example.payroll.adapters.outbound.persistence.contract.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "contract")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class ContractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_seq")
    @SequenceGenerator(name = "contract_seq", sequenceName = "contract_seq", allocationSize = 1)
    private Long id;

    private BigDecimal salary;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
}
