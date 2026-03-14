package com.vegas.sistema_gestion_operativa.common.context;

public class FranchiseContext {

    private static final ThreadLocal<Long> currentFranchiseId = new ThreadLocal<>();

    private FranchiseContext() {
    }

    public static void setCurrentFranchiseId(Long id) {
        currentFranchiseId.set(id);
    }

    public static Long getCurrentFranchiseId() {
        return currentFranchiseId.get();
    }

    public static void clear() {
        currentFranchiseId.remove();
    }
}
