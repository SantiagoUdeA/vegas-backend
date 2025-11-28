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

    /**
     * Agrega una cantidad de stock al inventario y recalcula el costo promedio
     * utilizando la fórmula del **promedio ponderado**.
     *
     * <p>Este método está diseñado para mantener la consistencia del inventario
     * cuando ingresa nueva materia prima con un costo unitario potencialmente diferente
     * al costo promedio actual. El cálculo del nuevo costo promedio se realiza usando
     * value objects (`Quantity` y `Money`) para garantizar exactitud y evitar manejar
     * tipos numéricos primitivos que puedan introducir errores.
     *
     * <p><b>Fórmula utilizada (Promedio Ponderado):</b><br>
     *
     * <pre>
     *   costoPromedioNuevo =
     *       (costoPromedioActual * stockActual + costoUnitarioEntrada * cantidadEntrada)
     *       ---------------------------------------------------------------------------
     *                              (stockActual + cantidadEntrada)
     * </pre>
     *
     * <p><b>Pasos que realiza el método:</b>
     * <ul>
     *   <li>Calcula el valor total del inventario actual (stock * costo promedio).</li>
     *   <li>Calcula el valor total de la nueva entrada (cantidad * costo unitario).</li>
     *   <li>Obtiene el valor total combinado del inventario.</li>
     *   <li>Suma la cantidad ingresada al stock existente.</li>
     *   <li>Divide el valor total del inventario entre el nuevo stock para obtener
     *       el nuevo costo promedio.</li>
     *   <li>Actualiza el estado interno del inventario.</li>
     * </ul>
     *
     * @param quantity Cantidad ingresada al inventario.
     * @param unitCost Costo unitario de la materia prima ingresada.
     * @throws ArithmeticException si se intenta dividir entre cero (solo posible si los value objects lo permiten).
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

        // Calcular nuevo costo promedio usando los value objects
        Money newAverageCost = totalValue.divide(newStock);

        this.currentStock = newStock;
        this.averageCost = newAverageCost;
    }

    /**
     * Agrega una cantidad de stock al inventario.
     *
     * @param quantity Cantidad a agregar.
     */
    public void addStock(Quantity quantity) {
        this.currentStock = this.currentStock.add(quantity);
    }

    /**
     * Reduce el stock del inventario restando la cantidad indicada.
     *
     * @param quantity Cantidad a reducir.
     * @throws NotEnoughRawMaterialStockException Si no hay suficiente stock para realizar la operación.
     */
    public void reduceStock(Quantity quantity) throws NotEnoughRawMaterialStockException {
        if (this.currentStock.getValue().compareTo(quantity.getValue()) < 0) {
            throw new NotEnoughRawMaterialStockException("No hay suficiente stock para realizar esta operación.");
        }
        this.currentStock = this.currentStock.subtract(quantity);
    }


}