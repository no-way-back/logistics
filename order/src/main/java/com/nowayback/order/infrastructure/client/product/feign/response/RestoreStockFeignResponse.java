package com.nowayback.order.infrastructure.client.product.feign.response;

import com.nowayback.order.application.client.response.RestoreStockResponse;

public record RestoreStockFeignResponse(
    boolean success
) {

    public RestoreStockResponse toApplicationResponse() {
        return new RestoreStockResponse(success);
    }
}