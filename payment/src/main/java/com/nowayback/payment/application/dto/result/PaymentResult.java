package com.nowayback.payment.application.dto.result;

import com.nowayback.payment.domain.entity.Payment;
import com.nowayback.payment.domain.vo.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public record PaymentResult (
        UUID id,
        UUID orderId,
        UUID userId,
        BigDecimal amount,
        PaymentStatus status,
        Optional<LocalDateTime> approvedAt,
        Optional<LocalDateTime> failedAt,
        Optional<LocalDateTime> canceledAt
) {

    public static PaymentResult from(Payment payment) {
        return new PaymentResult(
                payment.getId(),
                payment.getOrderId(),
                payment.getUserId(),
                payment.getAmount(),
                payment.getStatus(),
                Optional.ofNullable(payment.getApprovedAt()),
                Optional.ofNullable(payment.getFailedAt()),
                Optional.ofNullable(payment.getCanceledAt())

        );
    }
}
