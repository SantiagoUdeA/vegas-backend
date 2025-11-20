package com.vegas.sistema_gestion_operativa.common.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
@Getter
@NoArgsConstructor
public class Quantity {

    @Column(name = "quantity", precision = 19, scale = 4, nullable = false)
    private BigDecimal value;

    public Quantity(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        this.value = value.setScale(4, RoundingMode.HALF_UP);
    }

    public Quantity(Double value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        this.value = BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP);
    }

    public Quantity(Integer value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        this.value = BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP);
    }

    public Quantity add(Quantity other) {
        return new Quantity(this.value.add(other.value));
    }

    public Quantity subtract(Quantity other) {
        return new Quantity(this.value.subtract(other.value));
    }

    public Quantity multiply(Quantity other) {
        return new Quantity(this.value.multiply(other.value));
    }

    @JsonValue
    public BigDecimal getValue() {
        return value;
    }

}
