package com.nowayback.order.payment.domain.repository;

import com.nowayback.order.payment.domain.entity.Payment;

public interface PaymentRepository {

    void save(Payment payment);
}
