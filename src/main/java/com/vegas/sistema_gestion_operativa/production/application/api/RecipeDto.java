package com.vegas.sistema_gestion_operativa.production.application.api;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {

    private Long id;
    private Integer unitsProduced;
    private Boolean active;
    private String observations;
    private List<IngredientDto> ingredients;
    private Long productId;

}
