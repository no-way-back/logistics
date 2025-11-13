package com.nowayback.order.infrastructure.client.delivery;

import com.nowayback.order.application.client.DeliveryClient;
import com.nowayback.order.application.client.request.CreateDeliveryRequest;
import com.nowayback.order.application.client.response.CreateDeliveryResponse;
import com.nowayback.order.infrastructure.client.delivery.feign.DeliveryFeignClient;
import com.nowayback.order.infrastructure.client.delivery.feign.request.CreateDeliveryFeignRequest;
import com.nowayback.order.infrastructure.client.delivery.feign.response.CreateDeliveryFeignResponse;
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
        return CreateDeliveryResponse.of(
            response.deliveryId(),
            response.orderId(),
            response.status(),
            response.sourceHubId(),
            response.destinationHubId(),
            response.deliveryAddress(),
            response.recipientName(),
            response.recipientSlackId(),
            response.companyDeliveryManagerId()
        );
    }
}
