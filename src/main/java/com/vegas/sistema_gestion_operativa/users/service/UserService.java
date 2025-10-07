package com.vegas.sistema_gestion_operativa.users.service;

import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.users.factory.UserFactory;
import com.vegas.sistema_gestion_operativa.users.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for user operations.
 * Handles business logic for retrieving and creating users.
 */
@Service
public class UserService {

    /**
     * Repository for user persistence operations.
     */
    private final IUserRepository userRepository;

    /**
     * Constructor that injects the user repository.
     * @param userRepository user repository
     */
    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users from the repository.
     * @return list of users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Creates a new user from the provided DTO and saves it in the repository.
     * @param user DTO containing user data
     * @return the created user
     */
    public User create(CreateUserDto user) {
        var newUser = UserFactory.createFromDto(user);
        return userRepository.save(newUser);
    }

}
