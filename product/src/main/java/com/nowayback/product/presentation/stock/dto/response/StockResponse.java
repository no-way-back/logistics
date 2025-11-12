package com.nowayback.product.presentation.stock.dto.response;

import com.nowayback.product.application.stock.dto.StockResult;

import java.util.UUID;

public record StockResponse(
        UUID productId,
        int quantity
) {

    public static StockResponse from(StockResult result) {
        return new StockResponse(
                result.productId(),
                result.quantity()
        );
    }
}
