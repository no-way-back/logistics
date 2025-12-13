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
import com.nowayback.order.domain.event.entity.SagaStatus;
import com.nowayback.order.domain.event.vo.SagaState;
import com.nowayback.order.domain.repository.OrderRepository;
import com.nowayback.order.domain.repository.SagaStatusRepository;
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
    private final SagaStatusRepository sagaStatusRepository;

    @Transactional
    public void startOrderCreationSaga(OrderCreatedEvent event) {
        log.info("[SagaStateService.startOrderCreationSaga] {}", event);

        OrderCreatedEventPayload payload = event.getPayload();

        StockDecreaseEventPayload stockPayload = StockDecreaseEventPayload.from(payload);

        UUID sagaId = UUID.randomUUID();
        StockDecreaseEvent stockDecreaseEvent = StockDecreaseEvent.of(
            sagaId,
            event.getAggregateId(),
            stockPayload
        );

        sagaStatusRepository.save(
            SagaStatus.create(
                sagaId,
                payload.getOrderId()
            )
        );

        kafkaEventPublisher.publish(stockDecreaseEvent);
    }

    @Transactional
    public void handleStockDecreaseSucceeded(Event<StockDecreaseSucceedEventPayload> event) {
        log.info("[SagaStateService.handleStockDecreaseSucceeded] {}", event);

        UUID orderId = event.getPayload().getOrderId();

        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new OrderApplicationException(OrderApplicationErrorCode.ORDER_NOT_FOUND));

        OrderPaymentEvent orderPaymentEvent = OrderPaymentEvent.create(
            event.getSagaId(),
            OrderPaymentEventPayload.create(
                order.getId(),
                order.getCustomerId().getId(),
                order.getTotalPrice().longValue()
            )
        );

        updateSagaStatus(event.getSagaId(), SagaState.STOCK_REDUCED);

        log.info("[SagaStateService.handleStockDecreaseSucceeded] publish {}", orderPaymentEvent);
        kafkaEventPublisher.publish(orderPaymentEvent);
    }

    @Transactional
    public void handleStockDecreaseFailed(Event<StockDecreaseFailedEventPayload> event) {
        log.info("[SagaStateService.handleStockDecreaseFailed] {}", event);
        UUID orderId = event.getPayload().getOrderId();
        orderService.failedDecreaseStock(orderId);

        updateSagaStatus(event.getSagaId(), SagaState.STOCK_REDUCTION_FAILED);
    }

    @Transactional
    public void handlePaymentSucceeded(Event<OrderPaymentSucceededEventPayload> event) {
        log.info("[SagaStateService.handlePaymentSucceeded] {}", event);
        UUID orderId = event.getPayload().getOrderId();

        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new OrderApplicationException(OrderApplicationErrorCode.ORDER_NOT_FOUND));

        order.completeCreation();

        updateSagaStatus(event.getSagaId(), SagaState.PAYMENT_SUCCEEDED);
    }

    @Transactional
    public void handlePaymentFailed(Event<OrderPaymentFailedEventPayload> event) {
        log.info("[SagaStateService.handlePaymentFailed] {}", event);
        UUID orderId = event.getPayload().getOrderId();

        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new OrderApplicationException(OrderApplicationErrorCode.ORDER_NOT_FOUND));

        order.failPayment();

        updateSagaStatus(event.getSagaId(), SagaState.STOCK_COMPENSATING);

        publishStockIncreaseEvent(event.getSagaId(), orderId);
    }

    @Transactional
    public void handleStockIncreaseFailed(Event<StockIncreaseFailedEventPayload> event) {
        updateSagaStatus(event.getSagaId(), SagaState.STOCK_COMPENSATED_FAILED);
    }

    @Transactional
    public void handleStockIncreaseSucceeded(Event<StockIncreaseSucceedEventPayload> event) {
        log.info("[SagaStateService.handleStockIncreaseSucceeded] {}", event);

        updateSagaStatus(event.getSagaId(), SagaState.STOCK_COMPENSATED);
    }

    private void publishStockIncreaseEvent(UUID sagaId, UUID orderId) {
        log.info("[SagaStateService.publishStockIncreaseEvent] {}", orderId);

        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new OrderApplicationException(OrderApplicationErrorCode.ORDER_NOT_FOUND));

        kafkaEventPublisher.publish(
            StockIncreaseEvent.of(
                sagaId,
                order.getId(),
                StockIncreaseEventPayload.from(order)
            )
        );
    }

    private void updateSagaStatus(UUID sagaId, SagaState newStatus) {
        SagaStatus sagaStatus = sagaStatusRepository.findById(sagaId).orElseThrow();
        sagaStatus.updateState(newStatus);
    }
}
