package com.vegas.sistema_gestion_operativa.users.service;

import com.vegas.sistema_gestion_operativa.aws.service.CognitoIdentityService;
import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.users.exceptions.UserAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.users.factory.UserFactory;
import com.vegas.sistema_gestion_operativa.users.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;

import java.util.List;

/**
 * Service class for user operations.
 * Handles business logic for retrieving and creating users.
 */
@Service
public class UserService {

    private final CognitoIdentityService cognitoIdentityService;

    /**
     * Repository for user persistence operations.
     */
    private final IUserRepository userRepository;
    private final UserFactory userFactory;

    /**
     * Constructor that injects the user repository.
     * @param cognitoIdentityService AWS Cognito service integrated with aws SDK
     * @param userRepository user repository
     */
    @Autowired
    public UserService(CognitoIdentityService cognitoIdentityService, IUserRepository userRepository, UserFactory userFactory) {
        this.cognitoIdentityService = cognitoIdentityService;
        this.userRepository = userRepository;
        this.userFactory = userFactory;
    }

    /**
     * Retrieves all users from the repository.
     * @return list of users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Creates a new user from the provided DTO and saves it in the repository and then
     * uses cognito identity service to save it in Cognito.
     * @param user DTO containing user data
     * @return the created user
     */
    @Transactional
    public User create(CreateUserDto user) throws UserAlreadyExistsException {
        validateUserDoesntExists(user.email());
        String newUserId;
        try{
            newUserId = cognitoIdentityService.createUser(
                    user.email(),
                    user.givenName(),
                    user.familyName(),
                    user.roleName()
            );
        }catch (CognitoIdentityProviderException e){
            throw new RuntimeException(e.getMessage());
        }
        var newUser = userFactory.createFromDto(user, newUserId);
        return userRepository.save(newUser);
    }

    private void validateUserDoesntExists(String email) throws UserAlreadyExistsException {
        if(userRepository.findByEmail(email).isPresent()) throw new UserAlreadyExistsException(email);
    }

}
