package com.vegas.sistema_gestion_operativa.products_inventory.domain.entity;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.exceptions.InsufficientStockException;
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
     * Agrega stock al inventario
     *
     * @param quantity cantidad a agregar
     */
    public void addStock(Quantity quantity) {
        if(this.currentStock == null) this.currentStock = new Quantity(0.0);
        else this.currentStock = this.currentStock.add(quantity);

    }

    /**
     * Reduce stock del inventario
     *
     * @param quantity cantidad a reducir
     * @throws InsufficientStockException si no hay suficiente stock
     */
    public void reduceStock(Quantity quantity) throws InsufficientStockException {
        if (this.currentStock == null || this.currentStock.getValue().compareTo(quantity.getValue()) < 0) {
            throw new InsufficientStockException("No hay suficiente stock para realizar esta operación.");
        }
        this.currentStock = this.currentStock.subtract(quantity);
    }


}
