package com.nowayback.payment.application.dto.command;

import java.util.UUID;

public record ConfirmPaymentCommand (
        UUID orderId,
        String pgPaymentKey,
        String pgOrderId,
        String pgMethod
){
}
