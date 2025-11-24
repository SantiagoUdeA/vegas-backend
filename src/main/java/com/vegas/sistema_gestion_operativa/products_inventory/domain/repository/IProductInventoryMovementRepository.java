package com.vegas.sistema_gestion_operativa.products_inventory.domain.repository;

import com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductInventoryMovementDto;
import com.vegas.sistema_gestion_operativa.products_inventory.domain.entity.ProductInventoryMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProductInventoryMovementRepository extends JpaRepository<ProductInventoryMovement, Long> {
    @Query("""
        SELECT new com.vegas.sistema_gestion_operativa.products_inventory.application.dto.ProductInventoryMovementDto(
            pim.id,
            p.name,
            concat(u.givenName, ' ', u.familyName),
            pim.quantity,
            pim.movementReason,
            pim.justification,
            pim.movementDate
         )
        FROM ProductInventoryMovement pim
        JOIN Product p ON pim.productId = p.id
        JOIN User u ON pim.userId = u.id
        WHERE p.branchId = :branchId
    """)
    Page<ProductInventoryMovementDto> findAllByBranchId(Pageable pageable, Long branchId);
}
