package com.vegas.sistema_gestion_operativa.products.domain.repository;

import com.vegas.sistema_gestion_operativa.products.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    Page<Product> findByActiveTrue(Pageable pageable);

    Optional<Product> findByNameAndActiveTrue(String name);

    Optional<Product> findByNameAndActiveTrueAndBranchId(String name, Long branchId);

    @EntityGraph(
            attributePaths = {
                    "category",
                    "recipe",
                    "recipe.ingredients",
                    "recipe.ingredients.rawMaterial"
            },
            type = EntityGraph.EntityGraphType.FETCH
    )
    Page<Product> findByActiveTrueAndBranchId(Long branchId, Pageable pageable);

    @Query(value = "SELECT COALESCE(pi.current_stock, 0) FROM product_inventory pi WHERE pi.product_id = :productId", nativeQuery = true)
    Double findCurrentStockByProductId(@Param("productId") Long productId);
}
