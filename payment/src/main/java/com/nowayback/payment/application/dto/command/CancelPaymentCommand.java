package com.nowayback.payment.application.dto.command;

import java.util.UUID;

public record CancelPaymentCommand (
        UUID paymentId,
        String cancelReason
) {
}
