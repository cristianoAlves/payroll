package com.example.payroll.adapters.outbound.persistence.payroll.entity.tax;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "withholding_rate")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class WithholdingRateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal min;
    private BigDecimal max;
    private BigDecimal rate;

    @ManyToOne
    @JoinColumn(name = "tax_id")
    private TaxEntity tax;
}
