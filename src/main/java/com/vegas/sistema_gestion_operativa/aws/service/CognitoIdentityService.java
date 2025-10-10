    package com.vegas.sistema_gestion_operativa.aws.service;

    import com.vegas.sistema_gestion_operativa.aws.factory.CognitoIdentityRequestFactory;
    import jakarta.annotation.PostConstruct;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
    import software.amazon.awssdk.regions.Region;
    import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

    @Service
    public class CognitoIdentityService {

        private CognitoIdentityProviderClient client;

        @Value("${aws.cognito.userPoolId}")
        private String userPoolId;

        @Value("${aws.region}")
        private String region;

        @PostConstruct
        private void initializeClient() {
            System.out.println("Initializing CognitoIdentityService with region: " + region);
            this.client = CognitoIdentityProviderClient.builder()
                    .region(Region.of(region))
                    .build();
        }

        public void createUser(
                String email,
                String given_name,
                String family_name,
                String roleName
        ) {
            var request = CognitoIdentityRequestFactory.createAdminCreateUserRequest(
                    this.userPoolId,
                    given_name,
                    family_name,
                    email,
                    roleName.toUpperCase(),
                    true,
                    false
            );
            client.adminCreateUser(request);
        }
    }
