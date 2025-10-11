    package com.vegas.sistema_gestion_operativa.aws.service;

    import com.vegas.sistema_gestion_operativa.aws.factory.CognitoIdentityRequestFactory;
    import jakarta.annotation.PostConstruct;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
    import software.amazon.awssdk.regions.Region;
    import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
    import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;

    /**
     * Service for interacting with AWS Cognito Identity Provider.
     * Provides methods to create users in a specified user pool.
     */
    @Service
    public class CognitoIdentityService {

        private CognitoIdentityProviderClient client;

        @Value("${aws.cognito.userPoolId}")
        private String userPoolId;

        @Value("${aws.region}")
        private String region;

        /**
         * Initializes the CognitoIdentityProviderClient after the service is constructed.
         * Uses the configured AWS region.
         */
        @PostConstruct
        private void initializeClient() {
            System.out.println("Initializing CognitoIdentityService with region: " + region);
            this.client = CognitoIdentityProviderClient.builder()
                    .region(Region.of(region))
                    .build();
        }

        /**
         * Creates a new user in the Cognito user pool with the specified attributes.
         *
         * @param email      the email of the new user
         * @param given_name the given name (first name) of the new user
         * @param family_name the family name (last name) of the new user
         * @param roleName   the role to assign to the new user
         * @return the response from the AdminCreateUser operation
         */
        public AdminCreateUserResponse createUser(
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
            return client.adminCreateUser(request);
        }
    }
