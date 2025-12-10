package com.nowayback.payment.presentation.dto.response;

import com.nowayback.payment.application.dto.result.PaymentResult;
import com.nowayback.payment.domain.vo.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse (
        UUID id,
        UUID orderId,
        UUID userId,
        BigDecimal amount,
        PaymentStatus status,
        LocalDateTime approvedAt,
        LocalDateTime failedAt,
        LocalDateTime canceledAt
) {

    public static PaymentResponse from(PaymentResult result) {
        return new PaymentResponse(
                result.id(),
                result.orderId(),
                result.userId(),
                result.amount(),
                result.status(),
                result.approvedAt(),
                result.failedAt(),
                result.canceledAt()
        );
    }
}
