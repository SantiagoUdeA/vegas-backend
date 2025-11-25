package com.vegas.sistema_gestion_operativa.users.api;

public interface IUserApi {
    String getFullNameById(String userId);

    String getRoleById(String userId);
}
