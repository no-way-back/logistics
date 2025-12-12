package com.nowayback.product.application.event;

import com.nowayback.common.dataserializer.DataSerializer;
import com.nowayback.common.event.AggregateType;
import com.nowayback.common.event.Event;
import com.nowayback.common.event.EventHandler;
import com.nowayback.common.event.EventPayload;
import com.nowayback.product.application.event.payload.StockDecreaseEventPayload;
import com.nowayback.product.application.event.payload.StockDecreaseFailedEventPayload;
import com.nowayback.product.application.event.payload.StockDecreaseSucceedEventPayload;
import com.nowayback.product.application.stock.StockService;
import com.nowayback.product.application.stock.command.DecreaseStockCommand;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockDecreaseEventHandler implements EventHandler<StockDecreaseEventPayload> {

    private final StockService stockService;
    private final KafkaEventPublisher eventPublisher;

    @Override
    @Transactional
    public void handle(Event<StockDecreaseEventPayload> event) {
        StockDecreaseEventPayload payload = event.getPayload();
        try {
            payload.getItems().forEach(stock -> {
                log.info("[StockDecreaseEventHandler.handle] {}", stock.getProductId());
                stockService.decreaseStock(
                    DecreaseStockCommand.of(
                        stock.getProductId(),
                        stock.getQuantity()
                    )
                );
            });

            publishStockDecreaseSucceeded(event);
        } catch (Exception e) {
            publishStockDecreaseFailed(event, e.getMessage());
        }
    }

    @Override
    public boolean supports(Event<StockDecreaseEventPayload> event) {
        return event.getType() == ProductEventType.STOCK_DECREASE;
    }

    private void publishStockDecreaseSucceeded(Event<StockDecreaseEventPayload> sourceEvent) {
        StockDecreaseSucceedEventPayload payload =
            StockDecreaseSucceedEventPayload.from(sourceEvent.getPayload());

        Event<EventPayload> event = Event.of(
            UUID.randomUUID(),
            sourceEvent.getAggregateId(),
            ProductEventType.STOCK_DECREASE_SUCCEEDED,
            AggregateType.PRODUCT,
            payload
        );

        eventPublisher.publish(event);
    }

    private void publishStockDecreaseFailed(Event<StockDecreaseEventPayload> sourceEvent, String reason) {
        StockDecreaseFailedEventPayload payload =
            StockDecreaseFailedEventPayload.from(sourceEvent.getPayload(), reason);

        Event<EventPayload> event = Event.of(
            UUID.randomUUID(),
            sourceEvent.getAggregateId(),
            ProductEventType.STOCK_DECREASE_FAILED,
            AggregateType.PRODUCT,
            payload
        );

        eventPublisher.publish(event);
    }
}
