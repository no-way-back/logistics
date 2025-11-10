package com.nowayback.order.infrastructure.client.feign;

import com.nowayback.order.infrastructure.client.feign.request.CreateDeliveryFeignRequest;
import com.nowayback.order.infrastructure.client.feign.request.DecreaseStockFeignRequest;
import com.nowayback.order.infrastructure.client.feign.response.CreateDeliveryFeignResponse;
import com.nowayback.order.infrastructure.client.feign.response.DecreaseStockFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "delivery-service")
public interface DeliveryFeignClient {

    @PostMapping("/deliveries")
    CreateDeliveryFeignResponse createDelivery(
        @RequestBody CreateDeliveryFeignRequest request);
}
