package com.nowayback.order.application.event.handler;

import com.nowayback.common.event.Event;
import com.nowayback.common.event.EventHandler;
import com.nowayback.order.application.event.OrderEventType;
import com.nowayback.order.application.event.payload.StockIncreaseFailedEventPayload;
import com.nowayback.order.application.event.saga.SagaStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockIncreaseFailedEventHandler implements
    EventHandler<StockIncreaseFailedEventPayload> {

    private final SagaStateService sagaStateService;

    @Override
    public void handle(Event<StockIncreaseFailedEventPayload> event) {
        log.info("[StockIncreaseFailedEventHandler.handle] {}", event);
        sagaStateService.handleStockIncreaseFailed(event);
    }

    @Override
    public boolean supports(Event<StockIncreaseFailedEventPayload> event) {
        return event.getType() == OrderEventType.STOCK_INCREASE_FAILED;
    }
}
