package com.nowayback.order.application.event.saga;

import com.nowayback.common.event.Event;
import com.nowayback.order.application.OrderService;
import com.nowayback.order.application.event.OrderCreatedEvent;
import com.nowayback.order.application.event.OrderPaymentEvent;
import com.nowayback.order.application.event.StockDecreaseEvent;
import com.nowayback.order.application.event.StockIncreaseEvent;
import com.nowayback.order.application.event.payload.OrderCreatedEventPayload;
import com.nowayback.order.application.event.payload.OrderPaymentEventPayload;
import com.nowayback.order.application.event.payload.OrderPaymentFailedEventPayload;
import com.nowayback.order.application.event.payload.OrderPaymentSucceededEventPayload;
import com.nowayback.order.application.event.payload.StockDecreaseEventPayload;
import com.nowayback.order.application.event.payload.StockDecreaseFailedEventPayload;
import com.nowayback.order.application.event.payload.StockDecreaseSucceedEventPayload;
import com.nowayback.order.application.event.payload.StockIncreaseEventPayload;
import com.nowayback.order.application.event.payload.StockIncreaseFailedEventPayload;
import com.nowayback.order.application.event.payload.StockIncreaseSucceedEventPayload;
import com.nowayback.order.application.event.publisher.KafkaEventPublisher;
import com.nowayback.order.application.exception.OrderApplicationErrorCode;
import com.nowayback.order.application.exception.OrderApplicationException;
import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.domain.repository.OrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SagaStateService {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final KafkaEventPublisher kafkaEventPublisher;

    @Transactional
    public void startOrderCreationSaga(OrderCreatedEvent event) {
        log.info("[SagaStateService.startOrderCreationSaga] {}", event);

        OrderCreatedEventPayload payload = event.getPayload();

        StockDecreaseEventPayload stockPayload = StockDecreaseEventPayload.from(payload);

        StockDecreaseEvent stockDecreaseEvent = StockDecreaseEvent.of(
            event.getAggregateId(),
            stockPayload
        );

        kafkaEventPublisher.publish(stockDecreaseEvent);
    }

    public void handleStockDecreaseSucceeded(Event<StockDecreaseSucceedEventPayload> event) {
        log.info("[SagaStateService.handleStockDecreaseSucceeded] {}", event);

        UUID orderId = event.getPayload().getOrderId();

        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new OrderApplicationException(OrderApplicationErrorCode.ORDER_NOT_FOUND));

        OrderPaymentEvent orderPaymentEvent = OrderPaymentEvent.create(
            OrderPaymentEventPayload.create(
                order.getId(),
                order.getCustomerId().getId(),
                order.getTotalPrice().longValue()
            )
        );

        log.info("[SagaStateService.handleStockDecreaseSucceeded] publish {}", orderPaymentEvent);
        kafkaEventPublisher.publish(orderPaymentEvent);
    }

    public void handleStockDecreaseFailed(Event<StockDecreaseFailedEventPayload> event) {
        log.info("[SagaStateService.handleStockDecreaseFailed] {}", event);
        UUID orderId = event.getPayload().getOrderId();
        orderService.failedDecreaseStock(orderId);
    }

    @Transactional
    public void handlePaymentSucceeded(Event<OrderPaymentSucceededEventPayload> event) {
        log.info("[SagaStateService.handlePaymentSucceeded] {}", event);
        UUID orderId = event.getPayload().getOrderId();

        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new OrderApplicationException(OrderApplicationErrorCode.ORDER_NOT_FOUND));

        order.completeCreation();
    }

    @Transactional
    public void handlePaymentFailed(Event<OrderPaymentFailedEventPayload> event) {
        log.info("[SagaStateService.handlePaymentFailed] {}", event);
        UUID orderId = event.getPayload().getOrderId();

        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new OrderApplicationException(OrderApplicationErrorCode.ORDER_NOT_FOUND));

        order.failPayment();

        publishStockIncreaseEvent(orderId);
    }

    public void handleStockIncreaseFailed(Event<StockIncreaseFailedEventPayload> event) {

    }

    public void handleStockIncreaseSucceeded(Event<StockIncreaseSucceedEventPayload> event) {
        log.info("[SagaStateService.handleStockIncreaseSucceeded] {}", event);

        Order order = orderRepository.findById(event.getPayload().getOrderId()).orElseThrow(
            () -> new OrderApplicationException(OrderApplicationErrorCode.ORDER_NOT_FOUND));
    }

    private void publishStockIncreaseEvent(UUID orderId) {
        log.info("[SagaStateService.publishStockIncreaseEvent] {}", orderId);

        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new OrderApplicationException(OrderApplicationErrorCode.ORDER_NOT_FOUND));

        kafkaEventPublisher.publish(
            StockIncreaseEvent.of(
                order.getId(),
                StockIncreaseEventPayload.from(order)
            )
        );
    }
}
