package com.vegas.sistema_gestion_operativa.common.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@Builder
@NoArgsConstructor
public class PaginationRequest {

    private static final Integer MAX_PAGE_SIZE = 25;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    @Builder.Default
    private String sortField = "id";

    @Builder.Default
    private Sort.Direction direction = Sort.Direction.DESC;

    public PaginationRequest(Integer page, Integer size, String sortField, Sort.Direction direction) {
        this.page = page;
        this.size = (size != null && size > MAX_PAGE_SIZE) ? MAX_PAGE_SIZE : size;
        this.sortField = sortField;
        this.direction = direction;
    }

    public void setSize(Integer size) {
        this.size = (size != null && size > MAX_PAGE_SIZE) ? MAX_PAGE_SIZE : size;
    }
}