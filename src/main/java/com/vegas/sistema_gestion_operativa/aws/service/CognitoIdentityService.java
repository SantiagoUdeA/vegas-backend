    package com.vegas.sistema_gestion_operativa.aws.service;

    import com.vegas.sistema_gestion_operativa.aws.dto.CreateUserDto;
    import com.vegas.sistema_gestion_operativa.aws.dto.ListUsersDto;
    import com.vegas.sistema_gestion_operativa.aws.dto.ListUsersResponseDto;
    import com.vegas.sistema_gestion_operativa.aws.dto.UserDto;
    import com.vegas.sistema_gestion_operativa.aws.factory.CognitoIdentityRequestFactory;
    import com.vegas.sistema_gestion_operativa.aws.mapper.UserMapper;
    import jakarta.annotation.PostConstruct;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
    import software.amazon.awssdk.regions.Region;
    import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
    import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

    import java.util.List;

    @Service
    public class CognitoIdentityService {

        private CognitoIdentityProviderClient client;
        private final UserMapper userMapper;

        @Value("${aws.cognito.userPoolId}")
        private String userPoolId;

        @Value("${aws.region}")
        private String region;

        @Autowired
        public CognitoIdentityService(UserMapper userMapper) {
            this.userMapper = userMapper;
        }

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
                    dto.getRoleId(),
                    true,
                    false
            );
            return client.adminCreateUser(request);
        }

        public ListUsersResponseDto listUsersPage(ListUsersDto dto) {
            ListUsersRequest.Builder requestBuilder = ListUsersRequest.builder()
                    .userPoolId(userPoolId)
                    .limit(Math.min(dto.pageSize(), 10));

            if (dto.paginationToken() != null && !dto.paginationToken().isBlank()) {
                requestBuilder.paginationToken(dto.paginationToken());
            }

            var response = client.listUsers(requestBuilder.build());

            return mapToListUsersResponseDto(response);
        }

        private ListUsersResponseDto mapToListUsersResponseDto(ListUsersResponse response) {
            List<UserDto> users = response.users().stream()
                    .map(userMapper::toUserDto)
                    .toList();

            return new ListUsersResponseDto(
                    users,
                    response.paginationToken(),
                    response.paginationToken() != null
            );
        }
    }
