package com.nowayback.payment.application.dto.command;

import java.util.UUID;

public record CancelPaymentCommand (
        UUID paymentId,
        String cancelReason
) {

    public static CancelPaymentCommand of(
            UUID paymentId,
            String cancelReason
    ) {
        return new CancelPaymentCommand(
                paymentId,
                cancelReason
        );
    }
}
