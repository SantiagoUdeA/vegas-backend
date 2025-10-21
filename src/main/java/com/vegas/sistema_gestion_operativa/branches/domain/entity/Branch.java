package com.vegas.sistema_gestion_operativa.branches.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a branch in the system.
 * Contains branch details such as name, address, and phone number.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "branches")
@Getter
@Setter
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserBranch> userBranches = new HashSet<>();

    public void assignFounder(String founderId) {
        UserBranch relation = new UserBranch(founderId, this, true);
        userBranches.add(relation);
    }

    public void assignUser(String userId) {
        UserBranch relation = new UserBranch(userId, this, false);
        userBranches.add(relation);
    }

    public boolean isFoundedByUser(String userId) {
        return userBranches.stream()
                .anyMatch(ub -> ub.getUserId().equals(userId) && ub.isFounder());
    }

}
