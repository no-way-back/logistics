package com.nowayback.order.order.infrastructure.client.feign.response;

import com.nowayback.order.order.application.client.response.DecreaseStockResponse;

public record DecreaseStockFeignResponse(
    boolean success
) {

    public DecreaseStockResponse toApplicationResponse() {
        return new DecreaseStockResponse(success);
    }
}
