package com.vegas.sistema_gestion_operativa.common.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import io.cucumber.java.eo.Do;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
@NoArgsConstructor
@Getter
public class Money {

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal value;

    public Money(BigDecimal value) {
        this.value = value;
    }

    public Money(Double value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        this.value = BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP);
    }

    public Money multiply(Quantity quantity) {
        return new Money(
                this.value.multiply(quantity.getValue())
                        .setScale(4, RoundingMode.HALF_UP)
        );
    }


    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money subtract(Money other) {
        return new Money(this.value.subtract(other.value));
    }

    public Money multiply(BigDecimal factor) {
        return new Money(this.value.multiply(factor));
    }

    public Money divide(Quantity quantity) {
        BigDecimal result = this.value.divide(
                quantity.getValue(),
                4,
                RoundingMode.HALF_UP
        );
        return new Money(result);
    }

    @JsonValue
    public BigDecimal getValue() {
        return value;
    }

}
