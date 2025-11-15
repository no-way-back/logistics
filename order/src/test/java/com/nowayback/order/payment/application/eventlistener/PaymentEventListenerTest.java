package com.nowayback.order.payment.application.eventlistener;

import static com.nowayback.order.fixture.OrderFixture.createInvalidOrderCommand;
import static com.nowayback.order.fixture.OrderFixture.createOrderCommand;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.nowayback.order.order.application.OrderService;
import com.nowayback.order.order.application.command.CreateOrderCommand;
import com.nowayback.order.order.domain.event.OrderCreatedEvent;
import com.nowayback.order.order.domain.exception.OrderDomainException;
import com.nowayback.order.order.domain.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PaymentEventListenerTest {

    @Autowired
    private OrderService orderService;

    @SpyBean
    private PaymentEventListener paymentEventListener;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("트랜잭션 롤백 시 이벤트가 발행되지 않는다")
    void whenTransactionRollback_EventShouldNotBePublished() {
        // given
        CreateOrderCommand invalidOrderCommand = createInvalidOrderCommand();// 예외를 발생시킬 요청

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(invalidOrderCommand))
            .isInstanceOf(OrderDomainException.class);

        verify(paymentEventListener, never())
            .handleOrderCreated(any(OrderCreatedEvent.class));
    }

    @Test
    @DisplayName("트랜잭션 커밋 성공 시 이벤트가 발행된다")
    void whenTransactionCommit_EventShouldBePublished() {
        // given
        CreateOrderCommand orderCommand = createOrderCommand();

        // when
        orderService.createOrder(orderCommand);

        // then
        verify(paymentEventListener, times(1))
            .handleOrderCreated(any(OrderCreatedEvent.class));
    }
}