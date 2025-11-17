package com.vegas.sistema_gestion_operativa.production.application.api;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {

    private Long id;
    private Quantity quantity;
    private String observations;
    private Long recipeId;
    private Long rawMaterialId;

}
