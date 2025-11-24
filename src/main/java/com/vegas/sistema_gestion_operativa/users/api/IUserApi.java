package com.vegas.sistema_gestion_operativa.users.api;

public interface IUserApi {
    String getFullNameById(Long userId);
    String getRoleById(Long userId);
}
