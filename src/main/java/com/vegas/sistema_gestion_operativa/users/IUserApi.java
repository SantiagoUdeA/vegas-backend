package com.vegas.sistema_gestion_operativa.users;

import com.vegas.sistema_gestion_operativa.users.domain.exceptions.UserNotFoundException;

public interface IUserApi {
    String getFullNameById(String userId);

    String getRoleById(String userId);

    /**
     * Resolves a user's ID from their registered email address.
     *
     * @param email the user's email
     * @return the user's ID
     * @throws UserNotFoundException if no user is found with that email
     */
    String getIdByEmail(String email) throws UserNotFoundException;
}
