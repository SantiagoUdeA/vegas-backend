package com.vegas.sistema_gestion_operativa.branches.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserBranchId implements Serializable {
    @Column(name = "user_id")
    private String userId;

    @Column(name = "branch_id")
    private Long branchId;
}
