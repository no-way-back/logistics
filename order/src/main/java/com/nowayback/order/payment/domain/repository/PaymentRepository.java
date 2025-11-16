package com.nowayback.order.payment.domain.repository;

import com.nowayback.order.payment.domain.entity.Payment;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository {

    void save(Payment payment);

    List<Payment> findByOrderId(UUID id);
}
