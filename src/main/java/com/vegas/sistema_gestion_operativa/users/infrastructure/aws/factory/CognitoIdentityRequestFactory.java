package com.vegas.sistema_gestion_operativa.users.infrastructure.aws.factory;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.Arrays;

/**
 * Factory class for creating AWS Cognito Identity requests.
 * Provides methods to create request objects for various Cognito operations.
 */
@Component
public class CognitoIdentityRequestFactory {

    /**
     * Creates an AdminCreateUserRequest with the specified parameters.
     *
     * @param userPoolId   the ID of the Cognito user pool
     * @param givenName    the given name (first name) of the user
     * @param familyName   the family name (last name) of the user
     * @param email        the email of the user
     * @param roleName     the role to assign to the user
     * @param emailVerified whether the email is verified
     * @param sendEmail    whether to send a welcome email to the user
     * @return an AdminCreateUserRequest object
     */
    public AdminCreateUserRequest createAdminCreateUserRequest(
            String userPoolId,
            String givenName,
            String familyName,
            String email,
            String roleName,
            Boolean emailVerified,
            Boolean sendEmail
    ) {
        var messageAction = sendEmail ? "RESEND" : "SUPPRESS";
        var emailVerifiedValue = emailVerified ? "true" : "false";
        return AdminCreateUserRequest.builder()
                .userPoolId(userPoolId)
                .username(email)
                .userAttributes(Arrays.asList(
                        AttributeType.builder().name("given_name").value(givenName).build(),
                        AttributeType.builder().name("family_name").value(familyName).build(),
                        AttributeType.builder().name("email_verified").value(emailVerifiedValue).build(),
                        AttributeType.builder().name("custom:role").value(roleName).build()
                ))
                .messageAction(messageAction) // Evita que se envíe el correo de bienvenida
                .build();
    }

    public AdminDisableUserRequest createAdminDisableUserRequest(
            String userPoolId,
            String username
    ) {
        return AdminDisableUserRequest.builder()
                .userPoolId(userPoolId)
                .username(username)
                .build();
    }

    public AdminSetUserPasswordRequest createAdminSetUserPasswordRequest(
            String userPoolId,
            String username,
            String password
    ){
        return AdminSetUserPasswordRequest.builder()
                .userPoolId(userPoolId)
                .username(username)
                .password(password)
                .permanent(false) // falso para que la contraseña requiera cambio en el primer login
                .build();
    }

    public AdminDeleteUserRequest createAdminDeleteUserRequest(String userPoolId, String userId) {
        return AdminDeleteUserRequest.builder()
                .userPoolId(userPoolId)
                .username(userId)
                .build();
    }

    public AdminUpdateUserAttributesRequest createAdminUpdateUserAttributesRequest(
            String userPoolId,
            String username,
            String roleName
    ) {
        AttributeType roleAttribute = AttributeType.builder()
                .name("custom:role")
                .value(roleName)
                .build();

        return AdminUpdateUserAttributesRequest.builder()
                .userPoolId(userPoolId)
                .username(username)
                .userAttributes(roleAttribute)
                .build();
    }
}
