package com.nowayback.order;

import static com.nowayback.order.fixture.OrderFixture.createOrderCommandWithOrderItems;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import com.nowayback.order.order.application.OrderService;
import com.nowayback.order.order.application.command.CreateOrderCommand;
import com.nowayback.order.order.application.command.CreateOrderCommand.CreateOrderItem;
import com.nowayback.order.order.application.dto.OrderCreateResult;
import com.nowayback.order.order.domain.entity.Order;
import com.nowayback.order.order.domain.repository.OrderRepository;
import com.nowayback.order.order.domain.vo.OrderStatus;
import com.nowayback.order.payment.domain.entity.Payment;
import com.nowayback.order.payment.domain.repository.PaymentRepository;
import com.nowayback.order.payment.domain.vo.PaymentStatus;
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
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class OrderFlowIntegrationTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("주문 생성부터 재고 차감까지 전체 플로우가 정상 동작한다")
    void fullOrderFlow_ShouldWorkCorrectly() throws InterruptedException {
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
        OrderCreateResult response = orderService.createOrder(orderCommand);

        // then
        // 1. 주문 생성 확인
        Order order = orderRepository.findById(response.orderId()).orElseThrow();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);

        await()
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                List<Payment> payments = paymentRepository.findByOrderId(order.getId());
                assertThat(payments).hasSize(1);
                assertThat(payments.get(0).getStatus()).isEqualTo(PaymentStatus.COMPLETED);

                Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
                assertThat(updatedProduct.getStock()).isEqualTo(90);
            });
    }

}

