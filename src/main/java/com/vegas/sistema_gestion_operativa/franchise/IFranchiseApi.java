package com.vegas.sistema_gestion_operativa.franchise;

import com.vegas.sistema_gestion_operativa.franchise.domain.exception.FranchiseNotFoundException;

public interface IFranchiseApi {

    void assertFranchiseExists(Long franchiseId) throws FranchiseNotFoundException;
}
