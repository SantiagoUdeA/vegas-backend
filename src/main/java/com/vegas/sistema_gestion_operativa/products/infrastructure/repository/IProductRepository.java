package com.vegas.sistema_gestion_operativa.products.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.products.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    Page<Product> findByActiveTrue(Pageable pageable);

    Optional<Product> findByNameAndActiveTrue(String name);
}
