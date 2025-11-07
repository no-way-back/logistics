package com.nowayback.common.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        int currentPage,
        int pageSize,
        int totalPages,
        long totalElements,
        String sortBy,
        boolean isAsc,
        List<T> items
) {
    public static <T> PageResponse<T> fromPage(Page<T> page, String sortBy, boolean isAsc) {
        return new PageResponse<>(
                page.getNumber() + 1,   // 1페이지부터
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                sortBy,
                isAsc,
                page.getContent()
        );
    }

    // 기본 fromPage (기본 정렬: createdAt DESC)
    public static <T> PageResponse<T> fromPage(Page<T> page) {
        return new PageResponse<>(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                "created_at",
                false,
                page.getContent()
        );
    }
}