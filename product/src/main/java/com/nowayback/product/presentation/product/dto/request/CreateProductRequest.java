package com.nowayback.product.presentation.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateProductRequest(
        @NotNull UUID supplierId,
        @NotNull UUID hubId,
        @NotBlank String name,
        int price
) {
}
