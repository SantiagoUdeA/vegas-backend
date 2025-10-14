package com.vegas.sistema_gestion_operativa.users.service;

import com.vegas.sistema_gestion_operativa.aws.service.CognitoIdentityService;
import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.users.dto.UpdateUserDto;
import com.vegas.sistema_gestion_operativa.users.exceptions.UserAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.users.exceptions.UserAlreadyInactiveException;
import com.vegas.sistema_gestion_operativa.users.exceptions.UserNotFoundException;
import com.vegas.sistema_gestion_operativa.users.factory.UserFactory;
import com.vegas.sistema_gestion_operativa.users.mapper.IUserMapper;
import com.vegas.sistema_gestion_operativa.users.repository.IUserRepository;
import jakarta.validation.Valid;
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
    private final IUserMapper userMapper;

    /**
     * Constructor that injects the user repository.
     * @param cognitoIdentityService AWS Cognito service integrated with aws SDK
     * @param userRepository user repository
     */
    @Autowired
    public UserService(CognitoIdentityService cognitoIdentityService, IUserRepository userRepository, UserFactory userFactory, IUserMapper userMapper) {
        this.cognitoIdentityService = cognitoIdentityService;
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.userMapper = userMapper;
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

    /**
     * Updates an existing user with the provided DTO.
     * @param userId ID of the user to update
     * @param dto DTO containing updated user data
     * @return the updated user
     * @throws UserNotFoundException if the user is not found
     */
    public User update(String userId, UpdateUserDto dto) throws UserNotFoundException {
        var user = retrieveUser(userId);
        var updatedUser = userMapper.partialUpdate(dto, user);
        return userRepository.save(updatedUser);
    }

    /**
     * Deactivates a user by their ID.
     * @param userId ID of the user to deactivate
     * @throws UserNotFoundException if the user is not found
     */
    public void desactivate(String userId) throws UserNotFoundException, UserAlreadyInactiveException {
        var user = retrieveUser(userId);
        if(!user.isActive()) throw new UserAlreadyInactiveException("El usuario ya se encuentra inactivo");
        cognitoIdentityService.disableUser(user.getEmail());
        user.setActive(false);
        userRepository.save(user);
    }

    private void validateUserDoesntExists(String email) throws UserAlreadyExistsException {
        if(userRepository.findByEmail(email).isPresent()) throw new UserAlreadyExistsException(email);
    }

    /**
     * Retrieves a user by their ID.
     * @param userId ID of the user to retrieve
     * @return the retrieved user
     * @throws UserNotFoundException if the user is not found
     */
    private User retrieveUser(String userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id %s not found".formatted(userId)));
    }

}
