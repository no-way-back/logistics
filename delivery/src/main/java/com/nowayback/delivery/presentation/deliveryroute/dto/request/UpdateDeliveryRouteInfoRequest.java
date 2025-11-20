package com.nowayback.delivery.presentation.deliveryroute.dto.request;

import jakarta.validation.constraints.PositiveOrZero;

public record UpdateDeliveryRouteInfoRequest(
        @PositiveOrZero int actualDistanceMeters,
        @PositiveOrZero int actualDurationMinutes
) {
}
