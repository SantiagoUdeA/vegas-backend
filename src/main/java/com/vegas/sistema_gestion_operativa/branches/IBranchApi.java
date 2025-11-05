package com.vegas.sistema_gestion_operativa.branches;

import java.util.List;

public interface IBranchApi {

    public List<Long> getUserBranches(String userId);
}
