package com.vegas.sistema_gestion_operativa.products.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.products.domain.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    Optional<ProductCategory> findByName(String name);
}

