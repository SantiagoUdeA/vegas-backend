package com.vegas.sistema_gestion_operativa.aws.factory;

import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;

import java.util.Arrays;

/**
 * Factory class for creating AWS Cognito Identity requests.
 * Provides methods to create request objects for various Cognito operations.
 */
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
    public static AdminCreateUserRequest createAdminCreateUserRequest(
            String userPoolId,
            String givenName,
            String familyName,
            String email,
            String roleName,
            Boolean emailVerified,
            Boolean sendEmail
    ) {
        System.out.println("Creating AdminCreateUserRequest for userPoolId: " + userPoolId + ", email: " + email + ", roleName: " + roleName);
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
}
