package com.vegas.sistema_gestion_operativa.security.config;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final CustomPermissionEvaluator permissionEvaluator;

    public CustomMethodSecurityExpressionHandler(CustomPermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
        // sincronizamos el permission evaluator del handler
        setPermissionEvaluator(permissionEvaluator);
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
                                                                              MethodInvocation invocation) {
        // Creamos y devolvemos nuestro CustomSecurityExpressionRoot
        CustomSecurityExpressionRoot root = new CustomSecurityExpressionRoot(authentication, permissionEvaluator);
        root.setThis(invocation.getThis());
        root.setPermissionEvaluator(getPermissionEvaluator());
        return root;
    }
}
