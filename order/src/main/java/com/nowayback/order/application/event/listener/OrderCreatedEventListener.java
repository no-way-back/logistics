package com.nowayback.order.application.event.listener;

import com.nowayback.order.application.event.saga.SagaStateService;
import com.nowayback.order.application.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedEventListener extends AsyncTransactionalEventListener<OrderCreatedEvent>{

    private final SagaStateService sagaStateService;

    @Override
    protected void processEvent(OrderCreatedEvent event) {
        log.info("[OrderCreatedEventListener.processEvent] event = {}", event);

        sagaStateService.startOrderCreationSaga(event);
    }
}
