package com.nowayback.order.payment.infrastructure.persistence;

import com.nowayback.order.payment.domain.entity.Payment;
import com.nowayback.order.payment.domain.repository.PaymentRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    @Override
    public void save(Payment payment) {
        paymentJpaRepository.save(payment);
    }

    @Override
    public List<Payment> findByOrderId(UUID id) {
        return paymentJpaRepository.findByOrderId(id);
    }
}
