package com.example.payroll.adapters.outbound.persistence.payroll.entity;

import com.example.payroll.adapters.outbound.persistence.employee.entity.EmployeeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "payroll")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payroll_seq")
    @SequenceGenerator(name = "payroll_seq", sequenceName = "payroll_seq", allocationSize = 1)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    private LocalDate period;
    private BigDecimal salary;
}
