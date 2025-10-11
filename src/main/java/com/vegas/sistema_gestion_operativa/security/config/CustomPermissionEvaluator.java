package com.vegas.sistema_gestion_operativa.security.config;

import com.vegas.sistema_gestion_operativa.roles.domain.Permission;
import com.vegas.sistema_gestion_operativa.roles.domain.Role;
import com.vegas.sistema_gestion_operativa.roles.factory.RoleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final RoleFactory roleFactory;

    @Autowired
    public CustomPermissionEvaluator(RoleFactory roleFactory) {
        this.roleFactory = roleFactory;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || permission == null) {
            return false;
        }

        String roleName = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse(null);

        if (roleName == null) {
            return false;
        }

        Role role;
        try {
            role = roleFactory.createRole(roleName);
        }catch (IllegalArgumentException e){
            return false;
        }

        try {
            Permission required = Permission.valueOf(permission.toString());
            return role.hasPermission(required);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, targetId, permission);
    }
}