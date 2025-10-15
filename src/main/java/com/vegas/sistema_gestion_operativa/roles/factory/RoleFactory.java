package com.vegas.sistema_gestion_operativa.roles.factory;

import com.vegas.sistema_gestion_operativa.roles.domain.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleFactory {

    public Role createRole(String roleName) throws IllegalArgumentException {
        try{
            return Role.valueOf(roleName);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Rol invalido: " + roleName);
        }
    }
}
