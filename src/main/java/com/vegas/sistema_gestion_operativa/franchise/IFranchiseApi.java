package com.vegas.sistema_gestion_operativa.franchise;

import com.vegas.sistema_gestion_operativa.franchise.domain.exception.FranchiseAccessDeniedException;
import com.vegas.sistema_gestion_operativa.franchise.domain.exception.FranchiseNotFoundException;

public interface IFranchiseApi {

    void assertFranchiseExists(Long franchiseId) throws FranchiseNotFoundException;

    void assertUserOwnsFranchise(String userId, Long franchiseId) throws FranchiseAccessDeniedException;

    String getFranchiseName(Long franchiseId);
}
