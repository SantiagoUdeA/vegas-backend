package com.vegas.sistema_gestion_operativa.products_inventory.domain.entity;

import com.vegas.sistema_gestion_operativa.common.domain.Money;
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
            column = @Column(name = "current_stock", precision = 19, scale = 4)
    )
    private Quantity currentStock;

    /*
     * Costo promedio del producto en inventario
     */
    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "average_cost", precision = 19, scale = 4)
    )
    private Money averageCost;

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

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * Agrega stock al inventario y recalcula el costo promedio ponderado
     *
     * @param quantity cantidad a agregar
     * @param unitCost costo unitario del lote entrante
     */
    public void addStock(Quantity quantity, Money unitCost) {
        // Calcular valor del inventario actual
        Money currentValue = this.averageCost.multiply(this.currentStock);

        // Calcular valor de la entrada
        Money entryValue = unitCost.multiply(quantity);

        // Calcular valor total del inventario
        Money totalValue = currentValue.add(entryValue);

        // Calcular nuevo stock
        Quantity newStock = this.currentStock.add(quantity);

        // Calcular nuevo costo promedio
        Money newAverageCost = totalValue.divide(newStock);

        this.currentStock = newStock;
        this.averageCost = newAverageCost;
    }

    /**
     * Reduce el stock del inventario
     *
     * @param quantity cantidad a reducir
     * @throws IllegalArgumentException si la cantidad es mayor al stock disponible
     */
    public void reduceStock(Quantity quantity) {
        if (this.currentStock.getValue().compareTo(quantity.getValue()) < 0) {
            throw new IllegalArgumentException("Stock insuficiente");
        }
        this.currentStock = this.currentStock.subtract(quantity);
    }
}
