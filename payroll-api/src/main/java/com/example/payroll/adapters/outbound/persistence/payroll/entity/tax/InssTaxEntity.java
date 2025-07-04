package com.example.payroll.adapters.outbound.persistence.payroll.entity.tax;

import static jakarta.persistence.CascadeType.ALL;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *  Represents the complete rule set for a given period, made up of all the brackets
 */
@Entity
@DiscriminatorValue("INSS")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class InssTaxEntity extends TaxEntity {

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    /**
     * Represents the full progressive structure for a tax,
     * each WithholdingRateEntity will be one bracket of the tax (e.g., 0.00 to 1412.00 at 7.5%)
     */
    @OneToMany(mappedBy = "tax", cascade = ALL, orphanRemoval = true)
    private List<WithholdingRateEntity> withholdings;
}
