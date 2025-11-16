package com.nowayback.order.payment.application.eventlistener;

import com.nowayback.order.common.event.AsyncTransactionalEventListener;
import com.nowayback.order.order.domain.event.OrderCreatedEvent;
import com.nowayback.order.payment.application.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener extends AsyncTransactionalEventListener<OrderCreatedEvent> {
    private final PaymentService paymentService;

    @Override
    protected void processEvent(OrderCreatedEvent event) {
        log.info("비동기 결제 처리 시작 - Thread: {}, 주문 ID: {}",
            Thread.currentThread().getName(),
            event.getOrderId()
        );

        paymentService.processPayment(
            event.getOrderId(),
            event.getReceiverCompanyId(),
            event.getOrderItems()
        );

        log.info("비동기 결제 처리 완료 - 주문 ID: {}", event.getOrderId());
    }
}
