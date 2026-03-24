package com.vegas.sistema_gestion_operativa.products.domain.repository;

import com.vegas.sistema_gestion_operativa.products.domain.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    Optional<ProductCategory> findByNameAndBranchId(String name, Long branchId);
    Page<ProductCategory> findAllByBranchId(Long branchId, Pageable pageable);
}

