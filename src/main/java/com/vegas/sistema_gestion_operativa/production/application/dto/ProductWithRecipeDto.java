package com.vegas.sistema_gestion_operativa.production.application.dto;

import com.vegas.sistema_gestion_operativa.common.domain.Quantity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithRecipeDto {
    private Long id;
    private String name;
    private Boolean active;
    private RecipeDto recipe;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipeDto {
        private Long id;
        private Integer unitsProduced;
        private Boolean active;
        private String observations;
        private Long productId;
        private List<IngredientDto> ingredients;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IngredientDto {
        private Long id;
        private String observations;
        private Long rawMaterialId;
        private Quantity quantity;
        private RawMaterialDto rawMaterial;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RawMaterialDto {
        private String name;
        private String unitOfMeasure;
    }
}