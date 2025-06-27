package com.example.payroll.adapters.outbound.persistence.payroll.entity.tax;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "taxes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tax_type")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class TaxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    /**
     * Only used by Flat Taxes
     * Represents a fixed percentage used to calculate the tax amount.
     * rate = 0.11, it means 11% tax.
     */
    private BigDecimal rate;

    //@Column(name="deduction_id")
    //private Long deductionId;

    //@Column(name="tax_type")
    //private String taxType;
}
