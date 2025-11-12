package com.nowayback.order.application.client.request;

import java.util.List;
import java.util.UUID;

public record RestoreStockRequest(
    List<RestoreStockItem> items
) {

    public static RestoreStockRequest of(List<RestoreStockItem> items) {
        return new RestoreStockRequest(items);
    }

    public record RestoreStockItem(
        UUID productId,
        Integer quantity
    ) {

        public static RestoreStockItem of(UUID productId, Integer quantity) {
            return new RestoreStockItem(productId, quantity);
        }
    }
}
