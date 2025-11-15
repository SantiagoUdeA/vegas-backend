package com.vegas.sistema_gestion_operativa.branches;


import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;

public interface IBranchApi {

    void assertUserHasAccessToBranch(String userId, Long branchId) throws AccessDeniedException;
}
