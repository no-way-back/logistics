package com.nowayback.order.application.event.handler;

import com.nowayback.common.event.Event;
import com.nowayback.common.event.EventHandler;
import com.nowayback.order.application.event.OrderEventType;
import com.nowayback.order.application.event.payload.StockIncreaseSucceedEventPayload;
import com.nowayback.order.application.event.saga.SagaStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockIncreaseSucceedEventHandler implements EventHandler<StockIncreaseSucceedEventPayload> {

    private final SagaStateService sagaStateService;

    @Override
    public void handle(Event<StockIncreaseSucceedEventPayload> event) {
        log.info("[StockIncreaseSucceedEventHandler.handle] {}", event);
        sagaStateService.handleStockIncreaseSucceeded(event);
    }

    @Override
    public boolean supports(Event<StockIncreaseSucceedEventPayload> event) {
        return event.getType() == OrderEventType.STOCK_INCREASE_SUCCEEDED;
    }
}
