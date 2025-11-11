package com.nowayback.delivery.presentation.dto.request;

import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateDeliveryStatusRequest(
        @NotNull DeliveryStatus status
) {
}
