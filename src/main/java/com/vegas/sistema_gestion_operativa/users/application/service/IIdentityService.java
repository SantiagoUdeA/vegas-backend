package com.vegas.sistema_gestion_operativa.users.application.service;

/**
 * Interface for identity provider service operations.
 * Defines methods for user management in an external identity provider.
 */
public interface IIdentityService {

    /**
     * Creates a new user in the identity provider with the specified attributes.
     *
     * @param email       the email of the new user
     * @param givenName   the given name (first name) of the new user
     * @param familyName  the family name (last name) of the new user
     * @param roleName    the role to assign to the new user
     * @param idNumber    the identification number of the user
     * @return the ID of the created user
     */
    String createUser(String email, String givenName, String familyName, String roleName, String idNumber);

    /**
     * Disables a user in the identity provider.
     *
     * @param username the username or email of the user to disable
     */
    void disableUser(String username);

    /**
     * Enables a user in the identity provider.
     *
     * @param username the username or email of the user to enable
     */
    void enableUser(String username);

    /**
     * Sets a permanent password for a user in the identity provider.
     *
     * @param username the username or email of the user
     * @param password the new password to set
     */
    void setUserPassword(String username, String password);

    /**
     * Updates the role of a user in the identity provider.
     *
     * @param username    the username or email of the user
     * @param newRoleName the new role to assign to the user
     */
    void updateUserRole(String username, String newRoleName);

    /**
     * Deletes a user from the identity provider.
     *
     * @param userId the ID of the user to delete
     */
    void deleteUser(String userId);
}

