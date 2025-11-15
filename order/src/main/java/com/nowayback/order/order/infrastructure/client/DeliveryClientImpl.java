package com.nowayback.order.order.infrastructure.client;

import com.nowayback.order.order.application.client.DeliveryClient;
import com.nowayback.order.order.application.client.request.CreateDeliveryRequest;
import com.nowayback.order.order.application.client.response.CreateDeliveryResponse;
import com.nowayback.order.order.infrastructure.client.feign.DeliveryFeignClient;
import com.nowayback.order.order.infrastructure.client.feign.request.CreateDeliveryFeignRequest;
import com.nowayback.order.order.infrastructure.client.feign.response.CreateDeliveryFeignResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryClientImpl implements DeliveryClient {

    private final DeliveryFeignClient deliveryFeignClient;
    @Override
    public CreateDeliveryResponse createDelivery(CreateDeliveryRequest request) {
        CreateDeliveryFeignResponse response = deliveryFeignClient.createDelivery(
            CreateDeliveryFeignRequest.from(request));
        return response.toApplicationResponse();
    }
}
