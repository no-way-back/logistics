package com.nowayback.delivery.application.command;

import com.nowayback.delivery.domain.deliveryroute.vo.DeliveryId;
import com.nowayback.delivery.domain.deliveryroute.vo.HubId;
import com.nowayback.delivery.domain.deliveryroute.vo.HubRoute;
import com.nowayback.delivery.domain.deliveryroute.vo.RouteSequence;

import java.util.List;
import java.util.UUID;

public record CreateDeliveryRoutesCommand (
        DeliveryId deliveryId,
        List<DeliveryRouteSegment> segments
) {

    public static CreateDeliveryRoutesCommand of(
            UUID deliveryId,
            List<DeliveryRouteSegment> segments
    ) {
        return new CreateDeliveryRoutesCommand(
                DeliveryId.of(deliveryId),
                segments
        );
    }

    public record DeliveryRouteSegment(
            RouteSequence sequence,
            HubRoute hubRoute,
            int expectedDistanceMeters,
            int expectedDurationMinutes
    ) {
        public static DeliveryRouteSegment of(
                int sequence,
                UUID sourceHubId,
                UUID destinationHubId,
                int expectedDistanceMeters,
                int expectedDurationMinutes
        ) {
            return new DeliveryRouteSegment(
                    RouteSequence.of(sequence),
                    HubRoute.of(HubId.of(sourceHubId), HubId.of(destinationHubId)),
                    expectedDistanceMeters,
                    expectedDurationMinutes
            );
        }
    }
}
