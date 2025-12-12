package com.nowayback.order.application.event.saga;

import com.nowayback.common.event.Event;
import com.nowayback.order.application.event.StockDecreaseEvent;
import com.nowayback.order.application.event.payload.StockDecreaseEventPayload;
import com.nowayback.order.application.event.payload.StockDecreaseFailedEventPayload;
import com.nowayback.order.application.event.payload.StockDecreaseSucceedEventPayload;
import com.nowayback.order.application.event.publisher.KafkaEventPublisher;
import com.nowayback.order.application.event.OrderCreatedEvent;
import com.nowayback.order.application.event.payload.OrderCreatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SagaStateService {

    private final KafkaEventPublisher kafkaEventPublisher;

    @Transactional
    public void startOrderCreationSaga(OrderCreatedEvent event) {
        OrderCreatedEventPayload payload = event.getPayload();

        StockDecreaseEventPayload stockPayload = StockDecreaseEventPayload.from(payload);

        StockDecreaseEvent stockDecreaseEvent = StockDecreaseEvent.of(
            event.getAggregateId(),
            stockPayload
        );

        kafkaEventPublisher.publish(stockDecreaseEvent);
    }

    public void handleStockDecreaseSucceeded(Event<StockDecreaseSucceedEventPayload> event) {

    }

    public void handleStockDecreaseFailed(Event<StockDecreaseFailedEventPayload> event) {

    }
}
