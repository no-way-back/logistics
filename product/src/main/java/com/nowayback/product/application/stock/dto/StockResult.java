package com.nowayback.product.application.stock.dto;

import com.nowayback.product.domain.stock.entity.Stock;

import java.util.UUID;

public record StockResult(
        UUID productId,
        int quantity
) {
    public static StockResult from(Stock stock) {
        return new StockResult(
                stock.getProductId().getId(),
                stock.getQuantity().getQuantity()
        );
    }
}
