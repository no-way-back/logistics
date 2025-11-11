package com.nowayback.delivery.application.dto;

import com.nowayback.delivery.domain.deliveryroute.entity.DeliveryRoute;
import com.nowayback.delivery.domain.deliveryroute.vo.DeliveryRouteStatus;

import java.util.UUID;

public record DeliveryRouteResult(
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

    public static DeliveryRouteResult from(DeliveryRoute deliveryRoute) {
        return new DeliveryRouteResult(
                deliveryRoute.getId(),
                deliveryRoute.getSequence().getSequence(),
                deliveryRoute.getHubRoute().getSourceHubId().getId(),
                deliveryRoute.getHubRoute().getSourceHubId().getId(),
                deliveryRoute.getDeliveryManagerId().getId(),
                deliveryRoute.getStatus(),
                deliveryRoute.getRouteInfo().getExpectedDistanceMeters(),
                deliveryRoute.getRouteInfo().getExpectedDurationMinutes(),
                deliveryRoute.getRouteInfo().getActualDistanceMeters(),
                deliveryRoute.getRouteInfo().getActualDurationMinutes()
        );
    }
}
