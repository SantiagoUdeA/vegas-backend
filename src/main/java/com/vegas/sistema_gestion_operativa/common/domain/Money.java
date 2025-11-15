package com.vegas.sistema_gestion_operativa.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Money {

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal value;

    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money subtract(Money other) {
        return new Money(this.value.subtract(other.value));
    }

    public Money multiply(BigDecimal factor) {
        return new Money(this.value.multiply(factor));
    }
}
