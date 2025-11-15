package com.nowayback.order.order.application.client.request;

import java.util.List;
import java.util.UUID;

public record DecreaseStockRequest(
    List<DecreaseStockItem> items
) {
    public static DecreaseStockRequest of(List<DecreaseStockItem> items) {
        return new DecreaseStockRequest(items);
    }

    public record DecreaseStockItem(
        UUID productId,
        Integer quantity
    ) {
        public static DecreaseStockItem of(UUID productId, Integer quantity) {
            return new DecreaseStockItem(productId, quantity);
        }
    }
}
