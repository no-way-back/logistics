package com.nowayback.delivery.presentation.deliveryroute.dto.request;

import com.nowayback.delivery.domain.deliveryroute.vo.DeliveryRouteStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateDeliveryRouteStatusRequest(
        @NotNull DeliveryRouteStatus deliveryRouteStatus
) {
}
