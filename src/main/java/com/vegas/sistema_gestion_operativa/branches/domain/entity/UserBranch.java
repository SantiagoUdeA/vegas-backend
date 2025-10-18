package com.vegas.sistema_gestion_operativa.branches.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_branch")
@NoArgsConstructor
public class UserBranch {

    @EmbeddedId
    private UserBranchId id;

    @Getter
    private boolean founder;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("branchId")
    private Branch branch;

    @Column(name = "user_id", nullable = false,
            insertable = false, updatable = false,
            columnDefinition = "TEXT REFERENCES users(id)")
    private String userId;

    public UserBranch(String userId, Branch branch, boolean founder) {
        this.id = new UserBranchId(userId, branch.getId());
        this.branch = branch;
        this.founder = founder;
        this.userId = userId;
    }

    public String getUserId() {
        return id.getUserId();
    }
}
