package com.vegas.sistema_gestion_operativa.products_inventory.domain.repository;

import com.vegas.sistema_gestion_operativa.products_inventory.domain.entity.ProductInventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductInventoryMovementRepository extends JpaRepository<ProductInventoryMovement, Long> {
}
