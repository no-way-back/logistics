package com.nowayback.order.infrastructure.client.feign.request;

import com.nowayback.order.application.client.request.DecreaseStockRequest;
import com.nowayback.order.application.client.request.DecreaseStockRequest.DecreaseStockItem;
import java.util.List;
import java.util.UUID;

public record DecreaseStockFeignRequest(
    List<DecreaseStockFeignItem> items
) {
    public static DecreaseStockFeignRequest from(DecreaseStockRequest request) {
        return new DecreaseStockFeignRequest(
            request.items().stream()
                .map(DecreaseStockFeignItem::from)
                .toList()
        );
    }

    public record DecreaseStockFeignItem(
        UUID productId,
        Integer quantity
    ) {
        public static DecreaseStockFeignItem from(DecreaseStockItem decreaseStockItem) {
            return new DecreaseStockFeignItem(decreaseStockItem.productId(), decreaseStockItem.quantity());
        }
    }
}
