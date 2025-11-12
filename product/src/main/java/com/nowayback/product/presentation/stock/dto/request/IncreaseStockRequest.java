package com.nowayback.product.presentation.stock.dto.request;

import jakarta.validation.constraints.Positive;

public record IncreaseStockRequest(
        @Positive int amount
) {
}