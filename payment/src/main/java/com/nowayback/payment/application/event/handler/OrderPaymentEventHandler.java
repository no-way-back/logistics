package com.nowayback.payment.application.event.handler;

import com.nowayback.common.event.AggregateType;
import com.nowayback.common.event.Event;
import com.nowayback.common.event.EventHandler;
import com.nowayback.common.event.EventPayload;
import com.nowayback.payment.application.PaymentService;
import com.nowayback.payment.application.dto.command.ConfirmPaymentCommand;
import com.nowayback.payment.application.dto.command.CreatePaymentCommand;
import com.nowayback.payment.application.dto.result.PaymentResult;
import com.nowayback.payment.application.event.KafkaEventPublisher;
import com.nowayback.payment.application.event.PaymentEventType;
import com.nowayback.payment.application.event.paylaod.OrderPaymentEventPayload;
import com.nowayback.payment.application.event.paylaod.OrderPaymentFailedEventPayload;
import com.nowayback.payment.application.event.paylaod.OrderPaymentSucceededEventPayload;
import com.nowayback.payment.domain.vo.PaymentStatus;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentEventHandler implements EventHandler<OrderPaymentEventPayload> {

    private final PaymentService paymentService;
    private final KafkaEventPublisher eventPublisher;

    @Override
    @Transactional
    public void handle(Event<OrderPaymentEventPayload> event) {
        log.info("[OrderPaymentEventHandler.handle] {}", event);

        OrderPaymentEventPayload payload = event.getPayload();
        paymentService.createPayment(
            new CreatePaymentCommand(
                payload.getOrderId(),
                payload.getUserId(),
                BigDecimal.valueOf(payload.getAmount())
            )
        );

        PaymentResult paymentResult = paymentService.confirmPayment(
            new ConfirmPaymentCommand(
                payload.getOrderId(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
            )
        );

        publishPaymentResult(paymentResult);
    }

    @Override
    public boolean supports(Event<OrderPaymentEventPayload> event) {
        return PaymentEventType.ORDER_PAYMENT == event.getType();
    }

    private void publishPaymentResult(PaymentResult paymentResult) {
        if (paymentResult.status() == PaymentStatus.FAILED) {
            publishPaymentFailed(paymentResult);
            return;
        }

        publishPaymentSucceeded(paymentResult);
    }

    private void publishPaymentSucceeded(PaymentResult paymentResult) {
        Event<EventPayload> event = Event.of(
            UUID.randomUUID(),
            paymentResult.id(),
            PaymentEventType.ORDER_PAYMENT_SUCCEEDED,
            AggregateType.PAYMENT,
            OrderPaymentSucceededEventPayload.create(paymentResult.orderId())
        );

        eventPublisher.publish(event);
    }

    private void publishPaymentFailed(PaymentResult paymentResult) {
        Event<EventPayload> event = Event.of(
            UUID.randomUUID(),
            paymentResult.id(),
            PaymentEventType.ORDER_PAYMENT_FAILED,
            AggregateType.PAYMENT,
            OrderPaymentFailedEventPayload.create(paymentResult.orderId())
        );

        eventPublisher.publish(event);
    }

}
