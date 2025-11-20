package com.vegas.sistema_gestion_operativa.raw_material;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vegas.sistema_gestion_operativa.common.domain.UnitOfMeasure;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RawMaterialDto {

    private Long id;
    private String name;
    private UnitOfMeasure unitOfMeasure;
    private boolean active;
    private Long categoryId;
    private String categoryName;
    private Long branchId;

}
