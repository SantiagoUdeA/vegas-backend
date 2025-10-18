package com.vegas.sistema_gestion_operativa.users.application.service;

import com.vegas.sistema_gestion_operativa.roles.IRoleApi;
import com.vegas.sistema_gestion_operativa.users.domain.entity.User;
import com.vegas.sistema_gestion_operativa.users.application.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.users.application.dto.UpdateUserDto;
import com.vegas.sistema_gestion_operativa.users.domain.events.UserCreatedEvent;
import com.vegas.sistema_gestion_operativa.users.domain.exceptions.UserAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.users.domain.exceptions.UserAlreadyInactiveException;
import com.vegas.sistema_gestion_operativa.users.domain.exceptions.UserNotFoundException;
import com.vegas.sistema_gestion_operativa.users.application.factory.UserFactory;
import com.vegas.sistema_gestion_operativa.users.application.mapper.IUserMapper;
import com.vegas.sistema_gestion_operativa.users.infrastructure.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for user operations.
 * Handles business logic for retrieving and creating users.
 */
@Service
public class UserService {

    private final IIdentityService identityService;
    private final IRoleApi roleApi;
    private final IUserRepository userRepository;
    private final UserFactory userFactory;
    private final IUserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserService(
            IIdentityService identityService,
            IRoleApi roleApi,
            IUserRepository userRepository,
            UserFactory userFactory,
            IUserMapper userMapper,
            ApplicationEventPublisher eventPublisher
    ) {
        this.identityService = identityService;
        this.roleApi = roleApi;
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.userMapper = userMapper;
        this.eventPublisher = eventPublisher;
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
     * @param newUser DTO containing user data
     * @return the created user
     */
    @Transactional
    public User create(CreateUserDto newUser, String userId, String userRoleName) throws UserAlreadyExistsException {

        // Verificar si el usuario tiene permisos para crear un usuario con el rol especificado
        if(!this.roleApi.canCreateUserWithRole(userRoleName, newUser.roleName()))
            throw new AccessDeniedException("No tienes permisos para crear un usuario con el rol %s".formatted(newUser.roleName()));

        // Validar que el usuario no exista previamente
        validateUserDoesntExists(newUser.email());

        // Crear el usuario en el proveedor de identidad (Cognito), si hay error se hace rollback
        String newUserId = null;
        User savedUser;


        try {
            newUserId = identityService.createUser(
                    newUser.email(),
                    newUser.givenName(),
                    newUser.familyName(),
                    newUser.roleName(),
                    newUser.idNumber()
            );

            var userToSave = userFactory.createFromDto(newUser, newUserId);
            savedUser = userRepository.saveAndFlush(userToSave);

        } catch (Exception ex) {
            // Rollback manual si el usuario ya fue creado en Cognito
            if (Optional.ofNullable(newUserId).isPresent()) {
                identityService.deleteUser(newUserId);
            }
            throw ex; // Relanzar la exception original
        }

        eventPublisher.publishEvent(
                new UserCreatedEvent(userId, Optional.ofNullable(newUser.branchId()), newUser.roleName())
        );

        return savedUser;
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
        identityService.disableUser(user.getEmail());
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
    public User retrieveUser(String userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id %s not found".formatted(userId)));
    }

}
