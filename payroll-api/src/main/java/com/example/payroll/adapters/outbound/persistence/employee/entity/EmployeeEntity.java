package com.example.payroll.adapters.outbound.persistence.employee.entity;

import com.example.payroll.adapters.outbound.persistence.contract.entity.ContractEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "employee")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq")
    @SequenceGenerator(name = "employee_seq", sequenceName = "employee_seq", allocationSize = 1)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String cpf;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bank_account_id")
    private BankAccountEntity bankAccount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "employee_id")
    private List<ContractEntity> contracts = new ArrayList<>();
}
