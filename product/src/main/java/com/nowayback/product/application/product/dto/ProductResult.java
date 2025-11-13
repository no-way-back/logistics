package com.nowayback.product.application.product.dto;

import com.nowayback.product.domain.product.entity.Product;

import java.util.UUID;

public record ProductResult(
        UUID productId,
        UUID supplierId,
        UUID hubId,
        String name,
        int price,
        int quantity
) {

    public static ProductResult from(Product product, int quantity) {
        return new ProductResult(
                product.getId(),
                product.getSupplierId().getId(),
                product.getHubId().getId(),
                product.getProductInfo().getName(),
                product.getProductInfo().getPrice(),
                quantity
        );
    }
}
