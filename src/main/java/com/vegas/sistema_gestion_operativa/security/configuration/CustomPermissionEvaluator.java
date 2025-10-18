package com.vegas.sistema_gestion_operativa.security.configuration;

import com.vegas.sistema_gestion_operativa.roles.IRoleApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final IRoleApi roleApi;

    @Autowired
    public CustomPermissionEvaluator(IRoleApi roleApi) {
        this.roleApi = roleApi;
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

        return roleApi.hasPermission(roleName, permission.toString());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, targetId, permission);
    }
}