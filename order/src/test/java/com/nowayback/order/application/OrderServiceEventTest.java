package com.nowayback.order.application;

import static com.nowayback.order.fixture.OrderFixture.createOrderCommand;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.nowayback.order.order.application.OrderService;
import com.nowayback.order.order.application.command.CreateOrderCommand;
import com.nowayback.order.order.application.eventlistener.OrderEventListener;
import com.nowayback.order.order.domain.event.OrderCreatedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceEventTest {
    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderEventListener orderEventListener;

    @Test
    @DisplayName("주문 생성 시 OrderCreatedEvent가 발행된다")
    void createOrder_ShouldPublishOrderCreatedEvent() {
        // given
        CreateOrderCommand orderCommand = createOrderCommand();

        // when
        orderService.createOrder(orderCommand);

        // then
        // 이벤트 리스너가 호출되었는지 검증
        verify(orderEventListener, times(1))
            .processEvent(any(OrderCreatedEvent.class));
    }
}

