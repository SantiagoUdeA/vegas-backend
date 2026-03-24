package com.vegas.sistema_gestion_operativa.franchise.infrastructure.repository;

import com.vegas.sistema_gestion_operativa.franchise.domain.entity.OwnerFranchise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IOwnerFranchiseRepository extends JpaRepository<OwnerFranchise, Long> {

    @Query("SELECT of.franchiseId FROM OwnerFranchise of WHERE of.ownerId = :ownerId")
    Set<Long> findFranchiseIdsByOwnerId(String ownerId);

    @Query("SELECT of.ownerId FROM OwnerFranchise of WHERE of.franchiseId = :franchiseId")
    Set<String> findOwnerIdsByFranchiseId(Long franchiseId);

    boolean existsByOwnerIdAndFranchiseId(String ownerId, Long franchiseId);

    List<OwnerFranchise> findByOwnerId(String ownerId);

    void deleteByOwnerIdAndFranchiseId(String ownerId, Long franchiseId);
}
