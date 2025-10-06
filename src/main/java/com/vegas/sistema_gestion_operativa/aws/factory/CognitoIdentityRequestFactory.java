package com.vegas.sistema_gestion_operativa.aws.factory;

import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;

import java.util.Arrays;

public class CognitoIdentityRequestFactory {

    public static AdminCreateUserRequest createAdminCreateUserRequest(
            String userPoolId,
            String givenName,
            String familyName,
            String email,
            String roleId,
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
                        AttributeType.builder().name("custom:roleId").value(roleId).build()
                ))
                .messageAction(messageAction) // Evita que se envíe el correo de bienvenida
                .build();
    }
}
