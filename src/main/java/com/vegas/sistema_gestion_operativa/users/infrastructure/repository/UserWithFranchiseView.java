package com.vegas.sistema_gestion_operativa.users.infrastructure.repository;

public interface UserWithFranchiseView {
    String getId();
    String getEmail();
    String getGivenName();
    String getFamilyName();
    String getIdType();
    String getIdNumber();
    String getPhoneNumber();
    boolean isActive();
    String getRoleName();
    Long getFranchiseId();
    String getFranchiseName();
}
