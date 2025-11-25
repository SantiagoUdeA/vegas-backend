package com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.entity;

import com.vegas.sistema_gestion_operativa.common.domain.Money;
import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.raw_material_inventory.domain.exceptions.NotEnoughRawMaterialStockException;
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
public class RawMaterialInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "raw_material_id",
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT REFERENCES raw_material(id)"
    )
    private Long rawMaterialId;

    @Column(
            name = "branch_id",
            nullable = false,
            updatable = false,
            columnDefinition = "BIGINT REFERENCES branches(id)"
    )
    private Long branchId;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "current_stock", precision = 19, scale = 4)
    )
    private Quantity currentStock;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "average_cost", precision = 19, scale = 4)
    )
    private Money averageCost;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void addStock(Quantity quantity, Money unitCost) {
        // Calcular valor del inventario actual
        Money currentValue = this.averageCost.multiply(this.currentStock);

        // Calcular valor de la entrada
        Money entryValue = unitCost.multiply(quantity);

        // Calcular valor total del inventario
        Money totalValue = currentValue.add(entryValue);

        // Calcular nuevo stock
        Quantity newStock = this.currentStock.add(quantity);

        // Calcular nuevo costo promedio usando los value objects
        Money newAverageCost = totalValue.divide(newStock);

        this.currentStock = newStock;
        this.averageCost = newAverageCost;
    }

    public void reduceStock(Quantity quantity) throws NotEnoughRawMaterialStockException {
        if (this.currentStock.getValue().compareTo(quantity.getValue()) < 0) {
            throw new NotEnoughRawMaterialStockException("No hay suficiente stock para realizar esta operación.");
        }
        this.currentStock = this.currentStock.subtract(quantity);
    }


}