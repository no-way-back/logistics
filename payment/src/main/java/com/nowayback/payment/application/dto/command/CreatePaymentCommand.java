package com.nowayback.payment.application.dto.command;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentCommand (
        UUID orderId,
        UUID userId,
        BigDecimal amount
) {
}
