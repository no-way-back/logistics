package com.nowayback.order.application.client.request;

import java.util.List;
import java.util.UUID;

public record DecreaseStockRequest(
    List<StockItem> items
) {
    public static DecreaseStockRequest of(List<StockItem> items) {
        return new DecreaseStockRequest(items);
    }

    public record StockItem(
        UUID id,
        Integer quantity
    ) {
        public static StockItem of(UUID id, Integer quantity) {
            return new StockItem(id, quantity);
        }
    }
}
