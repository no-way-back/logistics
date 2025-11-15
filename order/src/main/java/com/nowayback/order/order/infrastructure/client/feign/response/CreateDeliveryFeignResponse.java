package com.nowayback.order.order.infrastructure.client.feign.response;

import com.nowayback.order.order.application.client.response.CreateDeliveryResponse;

public record CreateDeliveryFeignResponse(
    boolean success
) {

    public CreateDeliveryResponse toApplicationResponse() {
        return new CreateDeliveryResponse(success);
    }
}
