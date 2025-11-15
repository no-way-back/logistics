package com.nowayback.order.payment.application.eventlistener;

import static com.nowayback.order.fixture.OrderFixture.createInvalidOrderCommand;
import static com.nowayback.order.fixture.OrderFixture.createOrderCommand;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.nowayback.order.order.application.OrderService;
import com.nowayback.order.order.application.command.CreateOrderCommand;
import com.nowayback.order.order.domain.event.OrderCreatedEvent;
import com.nowayback.order.order.domain.exception.OrderDomainException;
import com.nowayback.order.order.domain.repository.OrderRepository;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
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

    @Test
    @DisplayName("이벤트가 비동기로 처리된다")
    void events_ShouldBeProcessedAsynchronously() throws InterruptedException {
        // given
        CreateOrderCommand orderCommand = createOrderCommand();
        String mainThread = Thread.currentThread().getName();
        log.info("메인 스레드: {}", mainThread);

        // when
        orderService.createOrder(orderCommand);

        // then
        await()
            .atMost(2, TimeUnit.SECONDS)
            .pollInterval(100, TimeUnit.MILLISECONDS)
            .untilAsserted(() -> {
                verify(paymentEventListener, times(1))
                    .handleOrderCreatedAsync(any(OrderCreatedEvent.class));
            });
    }

    @Test
    @DisplayName("비동기 처리 중 예외가 발생해도 메인 로직에 영향을 주지 않는다")
    void whenAsyncEventThrowsException_MainFlowShouldNotBeAffected() {
        // given
        CreateOrderCommand orderCommand = createOrderCommand();

        doThrow(new RuntimeException("결제 실패"))
            .when(paymentEventListener)
            .handleOrderCreatedAsync(any(OrderCreatedEvent.class));

        // when & then
        assertThatCode(() -> orderService.createOrder(orderCommand))
            .doesNotThrowAnyException();
    }
}