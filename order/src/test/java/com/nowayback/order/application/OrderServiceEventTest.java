package com.nowayback.order.application;

import static com.nowayback.order.fixture.OrderFixture.createOrderCommandWithOrderItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import com.nowayback.order.order.application.OrderService;
import com.nowayback.order.order.application.command.CreateOrderCommand;
import com.nowayback.order.order.application.command.CreateOrderCommand.CreateOrderItem;
import com.nowayback.order.order.application.eventlistener.OrderEventListener;
import com.nowayback.order.order.domain.event.OrderCreatedEvent;
import com.nowayback.order.product.domain.entity.Product;
import com.nowayback.order.product.domain.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceEventTest {
    @Autowired
    private OrderService orderService;

    @SpyBean
    private OrderEventListener orderEventListener;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("주문 생성 시 OrderCreatedEvent가 발행된다")
    void createOrder_ShouldPublishOrderCreatedEvent() {
        // given
        Product product = Product.builder()
            .id(UUID.randomUUID())
            .name("테스트 상품")
            .stock(100)
            .build();
        productRepository.save(product);

        CreateOrderCommand orderCommand = createOrderCommandWithOrderItems(List.of(
            CreateOrderItem.of(
                product.getId(),
                product.getName(),
                BigDecimal.valueOf(1000),
                10
            )
        ));

        // when
        orderService.createOrder(orderCommand);

        // then
        // 이벤트 리스너가 호출되었는지 검증
        await()
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                verify(orderEventListener, times(1))
                    .processEvent(any(OrderCreatedEvent.class));
            });
    }
}

