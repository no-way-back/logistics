package com.nowayback.order.infrastructure.client.delivery.feign;

import com.nowayback.order.infrastructure.client.delivery.feign.request.CreateDeliveryFeignRequest;
import com.nowayback.order.infrastructure.client.delivery.feign.response.CreateDeliveryFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "delivery-service")
public interface DeliveryFeignClient {

    @PostMapping("/deliveries")
    CreateDeliveryFeignResponse createDelivery(
        @RequestBody CreateDeliveryFeignRequest request);
}
