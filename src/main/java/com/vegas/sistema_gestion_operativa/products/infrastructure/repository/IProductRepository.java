package com.vegas.sistema_gestion_operativa.products.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.products.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    Page<Product> findByActiveTrue(Pageable pageable);

    Optional<Product> findByNameAndActiveTrue(String name);

    @Query(value = """
        SELECT DISTINCT p
        FROM Product p
        LEFT JOIN FETCH p.recipe r
        LEFT JOIN FETCH r.ingredients i
        LEFT JOIN FETCH i.rawMaterial rm
        WHERE p.active = true
        """,
        countQuery = """
        SELECT COUNT(DISTINCT p)
        FROM Product p
        LEFT JOIN p.recipe r
        WHERE p.active = true
        """)
    Page<Product> findAllProductsWithRecipes(Pageable pageable);
}
