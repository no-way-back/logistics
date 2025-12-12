package com.nowayback.product.application.event;

import com.nowayback.common.event.AggregateType;
import com.nowayback.common.event.Event;
import com.nowayback.common.event.EventHandler;
import com.nowayback.common.event.EventPayload;
import com.nowayback.product.application.event.payload.StockDecreaseEventPayload;
import com.nowayback.product.application.event.payload.StockDecreaseFailedEventPayload;
import com.nowayback.product.application.event.payload.StockDecreaseSucceedEventPayload;
import com.nowayback.product.application.event.payload.StockIncreaseEventPayload;
import com.nowayback.product.application.event.payload.StockIncreaseFailedEventPayload;
import com.nowayback.product.application.event.payload.StockIncreaseSucceedEventPayload;
import com.nowayback.product.application.stock.StockService;
import com.nowayback.product.application.stock.command.DecreaseStockCommand;
import com.nowayback.product.application.stock.command.IncreaseStockCommand;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockIncreaseEventHandler implements EventHandler<StockIncreaseEventPayload> {

    private final StockService stockService;
    private final KafkaEventPublisher eventPublisher;

    @Override
    @Transactional
    public void handle(Event<StockIncreaseEventPayload> event) {
        StockIncreaseEventPayload payload = event.getPayload();
        try {
            payload.getItems().forEach(stock -> {
                log.info("[StockIncreaseEventHandler.handle] {}", stock.getProductId());
                stockService.increaseStock(
                    IncreaseStockCommand.of(
                        stock.getProductId(),
                        stock.getQuantity()
                    )
                );
            });

            publishStockIncreaseSucceeded(event);
        } catch (Exception e) {
            publishStockIncreaseFailed(event, e.getMessage());
        }
    }

    @Override
    public boolean supports(Event<StockIncreaseEventPayload> event) {
        return event.getType() == ProductEventType.STOCK_INCREASE;
    }

    private void publishStockIncreaseSucceeded(Event<StockIncreaseEventPayload> sourceEvent) {
        StockIncreaseSucceedEventPayload payload =
            StockIncreaseSucceedEventPayload.from(sourceEvent.getPayload());

        Event<EventPayload> event = Event.of(
            UUID.randomUUID(),
            sourceEvent.getAggregateId(),
            ProductEventType.STOCK_INCREASE_SUCCEEDED,
            AggregateType.PRODUCT,
            payload
        );

        eventPublisher.publish(event);
    }

    private void publishStockIncreaseFailed(Event<StockIncreaseEventPayload> sourceEvent, String reason) {
        StockIncreaseFailedEventPayload payload =
            StockIncreaseFailedEventPayload.from(sourceEvent.getPayload(), reason);

        Event<EventPayload> event = Event.of(
            UUID.randomUUID(),
            sourceEvent.getAggregateId(),
            ProductEventType.STOCK_INCREASE_FAILED,
            AggregateType.PRODUCT,
            payload
        );

        eventPublisher.publish(event);
    }
}
