package com.nowayback.payment.presentation.dto.request;

import java.util.UUID;

public record CancelPaymentRequest (
        UUID paymentId,
        String cancelReason
) {
}
