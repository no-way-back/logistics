package com.nowayback.delivery.presentation.dto.request;

import com.nowayback.delivery.domain.deliveryroute.vo.DeliveryRouteStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateDeliveryRouteStatusRequest(
        @NotNull DeliveryRouteStatus deliveryRouteStatus
) {
}
