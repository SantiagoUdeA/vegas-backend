package com.vegas.sistema_gestion_operativa.security.filter;

import com.vegas.sistema_gestion_operativa.common.context.FranchiseContext;
import com.vegas.sistema_gestion_operativa.users.infrastructure.repository.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FranchiseResolutionFilter extends OncePerRequestFilter {

    private final IUserRepository userRepository;

    public FranchiseResolutionFilter(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
                String userId = jwt.getSubject();
                String role = jwt.getClaimAsString("role");

                if (!"ROOT".equals(role)) {
                    Long franchiseId = userRepository.findFranchiseIdByUserId(userId);
                    FranchiseContext.setCurrentFranchiseId(franchiseId);
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            FranchiseContext.clear();
        }
    }
}
