package com.nowayback.payment.presentation.dto.request;

import java.util.UUID;

public record ConfirmPaymentRequest (
        UUID orderId,
        String pgMethod,
        String pgPaymentKey,
        String pgOrderId
) {
}
