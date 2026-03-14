package com.vegas.sistema_gestion_operativa.common.configuration;

import com.vegas.sistema_gestion_operativa.common.context.FranchiseContext;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HibernateFilterInterceptor implements HandlerInterceptor {

    private final EntityManager entityManager;

    public HibernateFilterInterceptor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Long franchiseId = FranchiseContext.getCurrentFranchiseId();
        if (franchiseId != null) {
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter("franchiseFilter")
                    .setParameter("franchiseId", franchiseId);
        }
        return true;
    }
}
