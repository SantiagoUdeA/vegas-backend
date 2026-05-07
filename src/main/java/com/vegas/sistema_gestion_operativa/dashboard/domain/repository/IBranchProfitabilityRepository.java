package com.vegas.sistema_gestion_operativa.dashboard.domain.repository;

import com.vegas.sistema_gestion_operativa.dashboard.application.dto.BranchProfitabilityResponseDto;
import java.time.LocalDateTime;
import java.util.List;

public interface IBranchProfitabilityRepository {
    List<BranchProfitabilityResponseDto> getBranchProfitability(
            Long franchiseId,
            LocalDateTime from,
            LocalDateTime to
    );
}
