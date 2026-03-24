package com.vegas.sistema_gestion_operativa.security.filter;

import com.vegas.sistema_gestion_operativa.common.context.FranchiseContext;
import com.vegas.sistema_gestion_operativa.franchise.infrastructure.repository.IOwnerFranchiseRepository;
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
    private final IOwnerFranchiseRepository ownerFranchiseRepository;

    public FranchiseResolutionFilter(IUserRepository userRepository, IOwnerFranchiseRepository ownerFranchiseRepository) {
        this.userRepository = userRepository;
        this.ownerFranchiseRepository = ownerFranchiseRepository;
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
                    Long franchiseId = resolveFranchiseId(request, userId);
                    FranchiseContext.setCurrentFranchiseId(franchiseId);
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            FranchiseContext.clear();
        }
    }

    private Long resolveFranchiseId(HttpServletRequest request, String userId) {
        Long selectedFranchiseId = parseLong(request.getHeader("X-Franchise-Id"));

        if (selectedFranchiseId != null) {
            Long userFranchiseId = userRepository.findFranchiseIdByUserId(userId);
            boolean belongsDirectly = selectedFranchiseId.equals(userFranchiseId);
            boolean belongsAsOwner = ownerFranchiseRepository.existsByOwnerIdAndFranchiseId(userId, selectedFranchiseId);

            if (belongsDirectly || belongsAsOwner) {
                return selectedFranchiseId;
            }
        }

        Long userFranchiseId = userRepository.findFranchiseIdByUserId(userId);
        if (userFranchiseId != null) {
            return userFranchiseId;
        }

        return ownerFranchiseRepository.findFranchiseIdsByOwnerId(userId)
                .stream()
                .findFirst()
                .orElse(null);
    }

    private Long parseLong(String value) {
        try {
            return value == null ? null : Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
