package com.nowayback.delivery.presentation.dto.response;

import com.nowayback.delivery.application.deliveryroute.dto.DeliveryRouteResult;
import com.nowayback.delivery.domain.deliveryroute.vo.DeliveryRouteStatus;

import java.util.UUID;

public record DeliveryRouteResponse(
        UUID deliveryRouteId,
        int sequence,
        UUID sourceHubId,
        UUID destinationHubId,
        UUID hubDeliveryManagerId,
        DeliveryRouteStatus status,
        Integer expectedDistanceMeters,
        Integer expectedDurationMinutes,
        Integer actualDistanceMeters,
        Integer actualDurationMinutes
) {

    public static DeliveryRouteResponse from(DeliveryRouteResult result) {
        return new DeliveryRouteResponse(
                result.deliveryRouteId(),
                result.sequence(),
                result.sourceHubId(),
                result.destinationHubId(),
                result.hubDeliveryManagerId(),
                result.status(),
                result.expectedDistanceMeters(),
                result.expectedDurationMinutes(),
                result.actualDistanceMeters(),
                result.actualDurationMinutes()
        );
    }
}
