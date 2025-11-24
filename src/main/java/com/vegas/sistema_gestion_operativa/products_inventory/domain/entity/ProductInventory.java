package com.vegas.sistema_gestion_operativa.products_inventory.domain.entity;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "current_stock", precision = 19, scale = 4, nullable = false)
    )
    private Quantity currentStock = new Quantity(0.0);

    @Column(
            name = "branch_id",
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT REFERENCES branches(id)"
    )
    private Long branchId;

    @Column(
            name = "product_id",
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT REFERENCES product(id)"
    )
    private Long productId;

    /**
     * Costo promedio ponderado de inventario.
     */
    @Column(name = "average_cost", nullable = false)
    private Double averageCost = 0.0;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * Agrega stock al inventario
     */
    public void addStock(Quantity quantity) {
        if (this.currentStock == null) {
            this.currentStock = quantity;
        } else {
            this.currentStock = this.currentStock.add(quantity);
        }
    }

    /**
     * Resta stock al inventario
     */
    public void subtractStock(Quantity quantity) {
        if (this.currentStock == null) {
            throw new IllegalStateException("No hay stock inicializado para restar.");
        }
        this.currentStock = this.currentStock.subtract(quantity);
    }

    /**
     * Actualiza el costo promedio
     */
    public void updateAverageCost(Double newCost, double quantityAdded) {
        double stockActual = this.currentStock != null ? this.currentStock.getValue().doubleValue() : 0.0;

        if (stockActual <= 0) {
            this.averageCost = newCost;
            return;
        }

        // Fórmula costo promedio ponderado
        double costoPrevioTotal = this.averageCost * stockActual;
        double costoNuevoTotal = newCost * quantityAdded;

        this.averageCost = (costoPrevioTotal + costoNuevoTotal) / (stockActual + quantityAdded);
    }
}
