package com.vegas.sistema_gestion_operativa.aws.service;

import com.vegas.sistema_gestion_operativa.aws.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.aws.dto.ListUsersDto;
import com.vegas.sistema_gestion_operativa.aws.factory.CognitoIdentityRequestFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

@Service
public class CognitoIdentityService  {

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

    public AdminCreateUserResponse createUser(CreateUserDto dto) {
        var request = CognitoIdentityRequestFactory.createAdminCreateUserRequest(
                this.userPoolId,
                dto.getGiven_name(),
                dto.getFamily_name(),
                dto.getEmail(),
                true,
                false
        );
        return client.adminCreateUser(request);
    }

    public ListUsersResponse listUsersPage(ListUsersDto dto) {
        ListUsersRequest.Builder requestBuilder = ListUsersRequest.builder()
                .userPoolId(userPoolId)
                .limit(Math.min(dto.pageSize(), 10));

        if (dto.paginationToken() != null && !dto.paginationToken().isBlank()) {
            requestBuilder.paginationToken(dto.paginationToken());
        }

        var users = client.listUsers(requestBuilder.build());
        System.out.println(users.hasUsers());
        users.users().forEach(System.out::println);
        return users;
    }
}
