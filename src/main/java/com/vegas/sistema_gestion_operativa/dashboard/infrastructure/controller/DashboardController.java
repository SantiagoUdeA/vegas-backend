package com.vegas.sistema_gestion_operativa.dashboard.infrastructure.controller;

import com.vegas.sistema_gestion_operativa.dashboard.application.dto.BranchProfitabilityResponseDto;
import com.vegas.sistema_gestion_operativa.dashboard.application.service.BranchProfitabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final BranchProfitabilityService branchProfitabilityService;

    @GetMapping("/branch-profitability")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<BranchProfitabilityResponseDto>> getBranchProfitability(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(branchProfitabilityService.getBranchProfitability(from, to));
    }
}
