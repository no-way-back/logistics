package com.nowayback.order.payment.infrastructure.persistence;

import com.nowayback.order.payment.domain.entity.Payment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByOrderId(UUID id);
}
