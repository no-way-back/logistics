package com.nowayback.delivery.application.command;

public record UpdateDeliveryRouteInfoCommand(
        int actualDistanceMeters,
        int actualDurationMinutes
) {

    public static UpdateDeliveryRouteInfoCommand of(
            int actualDistanceMeters,
            int actualDurationMinutes
    ) {
        return new UpdateDeliveryRouteInfoCommand(
                actualDistanceMeters,
                actualDurationMinutes
        );
    }
}
