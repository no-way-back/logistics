package com.nowayback.delivery.presentation.dto.request;

import jakarta.validation.constraints.PositiveOrZero;

public record UpdateDeliveryRouteInfoRequest(
        @PositiveOrZero int actualDistanceMeters,
        @PositiveOrZero int actualDurationMinutes
) {
}
