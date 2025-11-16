package com.vegas.sistema_gestion_operativa.users.infrastructure.aws.service;

import com.vegas.sistema_gestion_operativa.users.application.service.IIdentityService;
import com.vegas.sistema_gestion_operativa.users.infrastructure.aws.factory.CognitoIdentityRequestFactory;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

/**
 * Service for interacting with AWS Cognito Identity Provider.
 * Provides methods to create users in a specified user pool.
 */
@Service
@Slf4j
public class CognitoIdentityService implements IIdentityService {

    private CognitoIdentityProviderClient client;
    private final CognitoIdentityRequestFactory cognitoIdentityRequestFactory;

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    @Value("${aws.region}")
    private String region;

    @Autowired
    public CognitoIdentityService(CognitoIdentityRequestFactory cognitoIdentityRequestFactory) {
        this.cognitoIdentityRequestFactory = cognitoIdentityRequestFactory;
    }

    /**
     * Initializes the CognitoIdentityProviderClient after the service is constructed.
     * Uses the configured AWS region.
     */
    @PostConstruct
    private void initializeClient() {
        log.info("Initializing CognitoIdentityService with region: {}", region);
        this.client = CognitoIdentityProviderClient.builder()
                .region(Region.of(region))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    /**
     * Creates a new user in the Cognito user pool with the specified attributes.
     * Sets its password as permanent with idNumber and does not send an invitation email.
     *
     * @param email      the email of the new user
     * @param givenName the given name (first name) of the new user
     * @param familyName the family name (last name) of the new user
     * @param roleName   the role to assign to the new user
     * @return the ID of the created user
     */
    public String createUser(
            String email,
            String givenName,
            String familyName,
            String roleName,
            String idNumber
    ) {
        var request = cognitoIdentityRequestFactory.createAdminCreateUserRequest(
                this.userPoolId,
                givenName,
                familyName,
                email,
                roleName.toUpperCase(),
                true,
                false
        );
        var userId = getUserId(client.adminCreateUser(request));
        setUserPassword(email, idNumber + "#Vegas");
        return userId;
    }

    /**
     * Disables a user in the Cognito user pool.
     *
     * @param username the username or email of the user to disable
     */
    public void disableUser(String username) {
        var request = cognitoIdentityRequestFactory.createAdminDisableUserRequest(
                this.userPoolId,
                username
        );
        client.adminDisableUser(request);
    }

    private String getUserId(AdminCreateUserResponse response) {
        return response.user().attributes().stream()
                .filter(attr -> "sub".equals(attr.name()))
                .findFirst()
                .map(AttributeType::value)
                .orElse(null);
    }

    /**
     * Sets a permanent password for a user in the Cognito user pool.
     *
     * @param username the username or email of the user
     * @param password the new password to set
     */
    public void setUserPassword(String username, String password) {
        var request = cognitoIdentityRequestFactory.createAdminSetUserPasswordRequest(
                this.userPoolId,
                username,
                password
        );
        client.adminSetUserPassword(request);
    }

    /**
     * Updates the role of a user in the Cognito user pool.
     *
     * @param username    the username or email of the user
     * @param newRoleName the new role to assign to the user
     */
    @Override
    public void updateUserRole(String username, String newRoleName) {
        var request = cognitoIdentityRequestFactory.createAdminUpdateUserAttributesRequest(
                this.userPoolId,
                username,
                newRoleName.toUpperCase()
        );
        client.adminUpdateUserAttributes(request);
    }

    /**
     * Deletes a user from the Cognito user pool.
     *
     * @param userId the ID of the user to delete
     */
    @Override
    public void deleteUser(String userId) {
        var request = cognitoIdentityRequestFactory.createAdminDeleteUserRequest(
                this.userPoolId,
                userId
        );
        client.adminDeleteUser(request);
    }
}
