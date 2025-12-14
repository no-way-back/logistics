package com.nowayback.order.application.event.handler;

import com.nowayback.common.event.Event;
import com.nowayback.common.event.EventHandler;
import com.nowayback.order.application.event.OrderEventType;
import com.nowayback.order.application.event.payload.StockDecreaseSucceedEventPayload;
import com.nowayback.order.application.event.saga.SagaStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockDecreaseSucceedEventHandler implements EventHandler<StockDecreaseSucceedEventPayload> {

    private final SagaStateService sagaStateService;

    @Override
    public void handle(Event<StockDecreaseSucceedEventPayload> event) {
        sagaStateService.handleStockDecreaseSucceeded(event);
    }

    @Override
    public boolean supports(Event<StockDecreaseSucceedEventPayload> event) {
        return event.getType() == OrderEventType.STOCK_DECREASE_SUCCEEDED;
    }
}
