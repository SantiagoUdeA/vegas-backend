package com.vegas.sistema_gestion_operativa.users.service;

import com.vegas.sistema_gestion_operativa.users.domain.User;
import com.vegas.sistema_gestion_operativa.users.dto.CreateUserDto;
import com.vegas.sistema_gestion_operativa.users.factory.UserFactory;
import com.vegas.sistema_gestion_operativa.users.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User create(CreateUserDto user) {
        var newUser = UserFactory.createFromDto(user);
        return userRepository.save(newUser);
    }

}
