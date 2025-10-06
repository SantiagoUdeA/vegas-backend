package com.vegas.sistema_gestion_operativa.security.config;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private final CustomPermissionEvaluator permissionEvaluator;
    private Object filterObject;
    private Object returnObject;
    private Object target;

    public CustomSecurityExpressionRoot(Authentication authentication, CustomPermissionEvaluator permissionEvaluator) {
        super(authentication);
        this.permissionEvaluator = permissionEvaluator;
    }

    // versión clásica
    public boolean hasPermission(Object target, Object permission) {
        return permissionEvaluator.hasPermission(getAuthentication(), target, permission);
    }

    // versión simplificada que queremos soportar
    public boolean hasPermission(Object permission) {
        return permissionEvaluator.hasPermission(getAuthentication(), null, permission);
    }

    // setters/getters requeridos por MethodSecurityExpressionOperations
    public void setThis(Object target) { this.target = target; }

    @Override
    public void setFilterObject(Object filterObject) { this.filterObject = filterObject; }

    @Override
    public Object getFilterObject() { return this.filterObject; }

    @Override
    public void setReturnObject(Object returnObject) { this.returnObject = returnObject; }

    @Override
    public Object getReturnObject() { return this.returnObject; }

    @Override
    public Object getThis() { return this.target; }
}