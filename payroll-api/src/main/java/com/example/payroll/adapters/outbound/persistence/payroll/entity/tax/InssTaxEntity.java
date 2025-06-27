package com.example.payroll.adapters.outbound.persistence.payroll.entity.tax;

import static jakarta.persistence.CascadeType.ALL;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@DiscriminatorValue("INSS")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class InssTaxEntity extends TaxEntity {

    @OneToMany(mappedBy = "tax", cascade = ALL, orphanRemoval = true)
    private List<WithholdingRateEntity> withholdings;
}
