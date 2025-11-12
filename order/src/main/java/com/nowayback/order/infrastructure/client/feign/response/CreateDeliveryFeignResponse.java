package com.nowayback.order.infrastructure.client.feign.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record CreateDeliveryFeignResponse(
    @JsonProperty("deliveryId") UUID deliveryId,
    @JsonProperty("orderId") UUID orderId,
    @JsonProperty("status") String status,
    @JsonProperty("sourceHubId") UUID sourceHubId,
    @JsonProperty("destinationHubId") UUID destinationHubId,
    @JsonProperty("deliveryAddress") String deliveryAddress,
    @JsonProperty("recipientName") String recipientName,
    @JsonProperty("recipientSlackId") String recipientSlackId,
    @JsonProperty("companyDeliveryManagerId") UUID companyDeliveryManagerId
) {}
