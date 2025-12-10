package com.nowayback.payment.infrastructure.repository;

import com.nowayback.payment.domain.entity.Payment;
import com.nowayback.payment.domain.vo.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<Payment, UUID> {
    boolean existsByOrderIdAndStatus(UUID orderId, PaymentStatus paymentStatus);
    Optional<Payment> findByOrderIdAndStatus(UUID orderId, PaymentStatus paymentStatus);
}
