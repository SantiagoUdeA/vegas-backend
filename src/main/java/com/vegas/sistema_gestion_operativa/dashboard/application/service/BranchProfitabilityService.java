package com.vegas.sistema_gestion_operativa.dashboard.application.service;

import com.vegas.sistema_gestion_operativa.common.context.FranchiseContext;
import com.vegas.sistema_gestion_operativa.dashboard.application.dto.BranchProfitabilityResponseDto;
import com.vegas.sistema_gestion_operativa.dashboard.domain.repository.IBranchProfitabilityRepository;
import com.vegas.sistema_gestion_operativa.security.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchProfitabilityService {

    private final IBranchProfitabilityRepository repository;

    @Transactional(readOnly = true)
    public List<BranchProfitabilityResponseDto> getBranchProfitability(LocalDate fromDate, LocalDate toDate) {
        Long franchiseId = AuthUtils.getFranchiseIdFromContext();

        if (franchiseId == null) {
            throw new IllegalStateException("Active franchise not set");
        }

        LocalDate to = (toDate != null) ? toDate : LocalDate.now();
        LocalDate from = (fromDate != null) ? fromDate : LocalDate.now().minusDays(30);

        LocalDateTime fromDateTime = from.atStartOfDay();
        LocalDateTime toDateTime = to.atTime(23, 59, 59);

        return repository.getBranchProfitability(franchiseId, fromDateTime, toDateTime);
    }
}
