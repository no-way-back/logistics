package com.nowayback.order.application.event.handler;

import com.nowayback.common.event.Event;
import com.nowayback.common.event.EventHandler;
import com.nowayback.order.application.event.OrderEventType;
import com.nowayback.order.application.event.payload.OrderPaymentSucceededEventPayload;
import com.nowayback.order.application.event.saga.SagaStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentSucceedEventHandler implements EventHandler<OrderPaymentSucceededEventPayload> {

    private final SagaStateService sagaStateService;

    @Override
    public void handle(Event<OrderPaymentSucceededEventPayload> event) {
        log.info("[PaymentSucceedEventHandler.handle] {}", event);

        sagaStateService.handlePaymentSucceeded(event);
    }

    @Override
    public boolean supports(Event<OrderPaymentSucceededEventPayload> event) {
        return OrderEventType.ORDER_PAYMENT_SUCCEEDED == event.getType();
    }
}
