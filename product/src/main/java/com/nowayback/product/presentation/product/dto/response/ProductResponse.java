package com.nowayback.product.presentation.product.dto.response;

import com.nowayback.product.application.product.dto.ProductResult;

import java.util.UUID;

public record ProductResponse(
        UUID productId,
        UUID supplierId,
        UUID hubId,
        String name,
        int price,
        int quantity
) {
    public static ProductResponse from(ProductResult result) {
        return new ProductResponse(
                result.productId(),
                result.supplierId(),
                result.hubId(),
                result.name(),
                result.price(),
                result.quantity()
        );
    }
}
