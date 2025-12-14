package com.nowayback.order.event;

import static com.nowayback.order.fixture.OrderFixture.createOrder;
import static org.assertj.core.api.Assertions.assertThat;

import com.nowayback.common.event.AggregateType;
import com.nowayback.common.event.Event;
import com.nowayback.common.event.EventPayload;
import com.nowayback.order.application.event.EventDispatcher;
import com.nowayback.order.application.event.OrderEventType;
import com.nowayback.order.application.event.payload.OrderPaymentFailedEventPayload;
import com.nowayback.order.application.event.payload.StockIncreaseSucceedEventPayload;
import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.domain.event.entity.SagaStatus;
import com.nowayback.order.domain.event.vo.SagaState;
import com.nowayback.order.domain.repository.OrderRepository;
import com.nowayback.order.domain.repository.SagaStatusRepository;
import com.nowayback.order.domain.vo.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EventFlowIntegrationTest {

    @Autowired
    private EventDispatcher eventDispatcher;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SagaStatusRepository sagaStatusRepository;

    @Test
    @DisplayName("결제 실패 이벤트 dispatch 시 주문 상태가 PAYMENT_FAILED로 변경되고, SagaStatus는 STOCK_COMPENSATING으로 변경된다")
    void 결제_실패_이벤트_dispatch_시_주문과_사가상태가_업데이트된다() {
        // given
        UUID sagaId = UUID.randomUUID();

        Order order = orderRepository.save(createOrder());
        UUID orderId = order.getId();

        sagaStatusRepository.save(
            SagaStatus.builder()
                .sagaId(sagaId)
                .orderId(orderId)
                .state(SagaState.PAYMENT_REQUESTED)
                .build()
        );

        Event<EventPayload> paymentFailedEvent = Event.of(
            sagaId,
            UUID.randomUUID(),
            orderId,
            OrderEventType.ORDER_PAYMENT_FAILED,
            AggregateType.ORDER,
            new OrderPaymentFailedEventPayload(orderId)
        );

        // when
        eventDispatcher.dispatch(paymentFailedEvent);

        // then
        Order updatedOrder = orderRepository.findById(orderId).orElseThrow();
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.PAYMENT_FAILED);

        SagaStatus updatedSaga = sagaStatusRepository.findById(sagaId).orElseThrow();
        assertThat(updatedSaga.getState()).isEqualTo(SagaState.STOCK_COMPENSATING);
    }

    @Test
    @DisplayName("재고 증가 성공 이벤트 dispatch 시 SagaStatus는 STOCK_COMPENSATED로 변경된다")
    void 재고_증가_성공_이벤트_dispatch_시_사가상태가_업데이트된다() {
        // given
        UUID sagaId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        sagaStatusRepository.save(
            SagaStatus.builder()
                .sagaId(sagaId)
                .orderId(orderId)
                .state(SagaState.STOCK_COMPENSATING)
                .build()
        );

        Event<EventPayload> stockIncreaseSucceededEvent = Event.of(
            sagaId,
            UUID.randomUUID(),
            orderId,
            OrderEventType.STOCK_INCREASE_SUCCEEDED,
            AggregateType.ORDER,
            new StockIncreaseSucceedEventPayload(orderId, List.of(), LocalDateTime.now())
        );

        // when
        eventDispatcher.dispatch(stockIncreaseSucceededEvent);

        // then
        SagaStatus updatedSaga = sagaStatusRepository.findById(sagaId).orElseThrow();
        assertThat(updatedSaga.getState()).isEqualTo(SagaState.STOCK_COMPENSATED);
    }
}
