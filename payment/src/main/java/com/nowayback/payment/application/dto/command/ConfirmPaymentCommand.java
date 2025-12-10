package com.nowayback.payment.application.dto.command;

import java.util.UUID;

public record ConfirmPaymentCommand (
        UUID orderId,
        String pgPaymentKey,
        String pgOrderId,
        String pgMethod
){

    public static ConfirmPaymentCommand of(
            UUID orderId,
            String pgMethod,
            String pgPaymentKey,
            String pgOrderId
    ) {
        return new ConfirmPaymentCommand(
                orderId,
                pgPaymentKey,
                pgOrderId,
                pgMethod
        );
    }
}
