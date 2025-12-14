package com.nowayback.order.application.event.handler;

import com.nowayback.common.event.Event;
import com.nowayback.common.event.EventHandler;
import com.nowayback.order.application.event.OrderEventType;
import com.nowayback.order.application.event.payload.OrderPaymentFailedEventPayload;
import com.nowayback.order.application.event.saga.SagaStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentFailedEventHandler implements EventHandler<OrderPaymentFailedEventPayload> {

    private final SagaStateService sagaStateService;

    @Override
    public void handle(Event<OrderPaymentFailedEventPayload> event) {
        log.info("[PaymentFailedEventHandler.handle] {}", event);

        sagaStateService.handlePaymentFailed(event);
    }

    @Override
    public boolean supports(Event<OrderPaymentFailedEventPayload> event) {
        return OrderEventType.ORDER_PAYMENT_FAILED == event.getType();
    }
}
