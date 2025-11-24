package com.vegas.sistema_gestion_operativa.users.api;

import com.vegas.sistema_gestion_operativa.users.domain.entity.User;
import com.vegas.sistema_gestion_operativa.users.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class IUserApiImpl implements IUserApi {

    private final UserRepository userRepository;

    public IUserApiImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getFullNameById(String userId) {
        return userRepository.findById(userId)
                .map(user -> user.getGivenName() + " " + user.getFamilyName())
                .orElse("Usuario desconocido");
    }

    @Override
    public String getRoleById(String userId) {
        return userRepository.findById(userId)
                .map(User::getRoleName)
                .orElse("Rol desconocido");
    }
}
