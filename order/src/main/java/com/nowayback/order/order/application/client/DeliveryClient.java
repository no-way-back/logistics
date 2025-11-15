package com.nowayback.order.application.client;


import com.nowayback.order.application.client.request.CreateDeliveryRequest;
import com.nowayback.order.application.client.response.CreateDeliveryResponse;

public interface DeliveryClient {

    CreateDeliveryResponse createDelivery(CreateDeliveryRequest request);
}
