package com.nowayback.order.infrastructure.client.product.feign.request;

import com.nowayback.order.application.client.request.RestoreStockRequest;
import com.nowayback.order.application.client.request.RestoreStockRequest.RestoreStockItem;
import java.util.List;
import java.util.UUID;

public record RestoreStockFeignRequest(
    List<RestoreStockFeignItem> items
) {

    public static RestoreStockFeignRequest from(RestoreStockRequest request) {
        return new RestoreStockFeignRequest(
            request.items().stream()
                .map(RestoreStockFeignItem::from)
                .toList()
        );
    }

    public record RestoreStockFeignItem(
        UUID productId,
        Integer quantity
    ) {

        public static RestoreStockFeignItem from(
            RestoreStockItem restoreStockItem) {
            return new RestoreStockFeignItem(
                restoreStockItem.productId(), restoreStockItem.quantity());
        }
    }
}
