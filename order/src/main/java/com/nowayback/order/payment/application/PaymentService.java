package com.nowayback.order.payment.application;

import com.nowayback.order.order.domain.vo.OrderItemSnapshot;
import com.nowayback.order.payment.domain.entity.Payment;
import com.nowayback.order.payment.domain.event.PaymentCompletedEvent;
import com.nowayback.order.payment.domain.repository.PaymentRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void processPayment(UUID orderId, UUID userId, List<OrderItemSnapshot> orderItems) {
        // 간단한 결제 처리 로직
        Payment payment = Payment.create(orderId, userId, calculateAmount(orderItems));
        payment.complete(); // 실무에서는 PG사 연동 등의 복잡한 로직이 들어갑니다

        paymentRepository.save(payment);

        // 결제 완료 이벤트 발행
        eventPublisher.publishEvent(PaymentCompletedEvent.of(payment, orderItems));
    }

    private BigDecimal calculateAmount(List<OrderItemSnapshot> orderItems) {
        return orderItems.stream()
            .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
