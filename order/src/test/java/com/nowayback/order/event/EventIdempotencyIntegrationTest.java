package com.nowayback.order.event;

import static com.nowayback.order.fixture.OrderFixture.createOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.nowayback.common.event.AggregateType;
import com.nowayback.common.event.Event;
import com.nowayback.common.event.EventPayload;
import com.nowayback.order.application.event.EventDispatcher;
import com.nowayback.order.application.event.OrderEventType;
import com.nowayback.order.application.event.payload.OrderPaymentFailedEventPayload;
import com.nowayback.order.application.event.publisher.KafkaEventPublisher;
import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.domain.event.entity.SagaStatus;
import com.nowayback.order.domain.event.vo.SagaState;
import com.nowayback.order.domain.repository.OrderRepository;
import com.nowayback.order.domain.repository.SagaStatusRepository;
import com.nowayback.order.infrastructure.persistence.ProcessedEventsJpaRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class EventIdempotencyIntegrationTest {

    @Autowired
    private EventDispatcher eventDispatcher;

    @Autowired
    private SagaStatusRepository sagaStatusRepository;

    @Autowired
    private OrderRepository orderRepository;

    @MockitoBean
    private KafkaEventPublisher kafkaEventPublisher;
    @Autowired
    ProcessedEventsJpaRepository processedEventsJpaRepository;

    @BeforeEach
    void setUp() {
        processedEventsJpaRepository.deleteAll();
    }
    @Test
    @DisplayName("같은 eventId로 결제 실패 이벤트를 2번 dispatch해도 StockIncreaseEvent publish는 1번만 된다(멱등)")
    void 결제실패_이벤트_멱등성_publish_1회() throws InterruptedException {
        // given
        UUID sagaId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID(); // ✅ 동일 eventId

        Order order = orderRepository.save(createOrder());
        orderId = order.getId();

        sagaStatusRepository.save(
            SagaStatus.builder()
                .sagaId(sagaId)
                .orderId(orderId)
                .state(SagaState.PAYMENT_REQUESTED)
                .build()
        );

        Event<EventPayload> paymentFailedEvent = Event.of(
            sagaId,
            eventId,
            orderId,
            OrderEventType.ORDER_PAYMENT_FAILED,
            AggregateType.ORDER,
            new OrderPaymentFailedEventPayload(orderId)
        );

        // when: 같은 eventId로 2번 dispatch
        eventDispatcher.dispatch(paymentFailedEvent);
        eventDispatcher.dispatch(paymentFailedEvent);

        // then: publish는 1번만
        then(kafkaEventPublisher).should(times(1)).publish(any());
    }
}