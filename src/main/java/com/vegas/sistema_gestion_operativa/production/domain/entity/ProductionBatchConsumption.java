package com.vegas.sistema_gestion_operativa.production.domain.entity;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "production_batch_consumption")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionBatchConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "production_id", nullable = false, columnDefinition = "BIGINT REFERENCES production(id)")
    private Long productionId;

    @Column(name = "raw_material_batch_id", nullable = false, columnDefinition = "BIGINT REFERENCES raw_material_batch(id)")
    private Long rawMaterialBatchId;

    @Column(name = "raw_material_id", nullable = false, columnDefinition = "BIGINT REFERENCES raw_material(id)")
    private Long rawMaterialId;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "quantity_consumed", precision = 19, scale = 4, nullable = false)
    )
    private Quantity quantityConsumed;
}
