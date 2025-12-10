package com.nowayback.payment.infrastructure.repository;

import com.nowayback.payment.domain.PaymentRepository;
import com.nowayback.payment.domain.entity.Payment;
import com.nowayback.payment.domain.vo.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository jpaRepository;

    @Override
    public Payment save(Payment payment) {
        return jpaRepository.save(payment);
    }

    @Override
    public boolean existsByOrderIdAndStatus(UUID orderId, PaymentStatus paymentStatus) {
        return jpaRepository.existsByOrderIdAndStatus(orderId, paymentStatus);
    }

    @Override
    public Optional<Payment> findById(UUID paymentId) {
        return jpaRepository.findById(paymentId);
    }

    @Override
    public Optional<Payment> findByOrderIdAndStatus(UUID orderId, PaymentStatus paymentStatus) {
        return jpaRepository.findByOrderIdAndStatus(orderId, paymentStatus);
    }
}
