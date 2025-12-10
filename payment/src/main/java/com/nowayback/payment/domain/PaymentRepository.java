package com.nowayback.payment.domain;

import com.nowayback.payment.domain.entity.Payment;
import com.nowayback.payment.domain.vo.PaymentStatus;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Payment save(Payment payment);
    boolean existsByOrderIdAndStatus(UUID orderId, PaymentStatus paymentStatus);
    Optional<Payment> findById(UUID paymentId);
    Optional<Payment> findByOrderIdAndStatus(UUID orderId, PaymentStatus paymentStatus);
}
