package com.nowayback.order.application;

import static com.nowayback.order.fixture.OrderFixture.createCancelOrderCommand;
import static com.nowayback.order.fixture.OrderFixture.createOrder;
import static com.nowayback.order.fixture.OrderFixture.createOrderCommand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.order.application.actor.OrderActorFactory;
import com.nowayback.order.application.client.DeliveryClient;
import com.nowayback.order.application.client.ProductClient;
import com.nowayback.order.application.client.response.CreateDeliveryResponse;
import com.nowayback.order.application.client.response.DecreaseStockResponse;
import com.nowayback.order.application.command.CancelOrderCommand;
import com.nowayback.order.application.command.CreateOrderCommand;
import com.nowayback.order.application.exception.OrderApplicationException;
import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.domain.exception.OrderDomainException;
import com.nowayback.order.domain.repository.OrderRepository;
import com.nowayback.order.domain.vo.OrderStatus;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    ProductClient productClient;

    @Mock
    DeliveryClient deliveryClient;

    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    OrderService orderService;

    @Nested
    class CreateOrder {
        @Test
        @DisplayName("재고 차감과 배송 생성이 완료되면 주문을 생성한다.")
        void createOrder_success() {
            // given
            CreateOrderCommand orderCommand = createOrderCommand();

            DecreaseStockResponse decreaseStockResponse = new DecreaseStockResponse(true);
            CreateDeliveryResponse createDeliveryResponse = new CreateDeliveryResponse(true);

            given(productClient.decreaseStocks(any())).willReturn(decreaseStockResponse);
            given(deliveryClient.createDelivery(any())).willReturn(createDeliveryResponse);

            // when
            orderService.createOrder(orderCommand);

            //then
            ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
            then(orderRepository).should().save(orderCaptor.capture());

            Order savedOrder = orderCaptor.getValue();
            assertThat(savedOrder.getSupplierCompanyId()).isEqualTo(orderCommand.supplierCompanyId());
            assertThat(savedOrder.getSupplierCompanySnapshot()).isEqualTo(orderCommand.supplierCompanySnapshot());
            assertThat(savedOrder.getReceiverCompanyId()).isEqualTo(orderCommand.receiverCompanyId());
            assertThat(savedOrder.getReceiverCompanySnapshot()).isEqualTo(
                orderCommand.receiverCompanySnapshot());
            assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.CREATED);
            assertThat(savedOrder.getRequest()).isEqualTo(orderCommand.request());
            assertThat(savedOrder.getOrderItems()).hasSize(orderCommand.createOrderItems().size());
        }

        @Test
        @DisplayName("재고 차감에 실패하면 예외가 발생한다.")
        void createOrder_shouldThrowWhenFailDecreaseStock() {
            // given
            CreateOrderCommand orderCommand = createOrderCommand();

            DecreaseStockResponse decreaseStockResponse = new DecreaseStockResponse(false);

            given(productClient.decreaseStocks(any())).willReturn(decreaseStockResponse);

            // when / then
            assertThatThrownBy(() -> {
                orderService.createOrder(orderCommand);
            }).isInstanceOf(OrderApplicationException.class);
        }

        @Test
        @DisplayName("배송 생성에 실패하면 예외가 발생한다.")
        void createOrder_shouldThrowWhenFailCreateDelivery() {
            // given
            CreateOrderCommand orderCommand = createOrderCommand();

            DecreaseStockResponse decreaseStockResponse = new DecreaseStockResponse(true);
            CreateDeliveryResponse createDeliveryResponse = new CreateDeliveryResponse(false);

            given(productClient.decreaseStocks(any())).willReturn(decreaseStockResponse);
            given(deliveryClient.createDelivery(any())).willReturn(createDeliveryResponse);

            // when / then
            assertThatThrownBy(() -> {
                orderService.createOrder(orderCommand);
            }).isInstanceOf(OrderApplicationException.class);
        }
    }

    @Nested
    class CancelOrder {

        @Test
        @DisplayName("주문을 취소한다")
        void cancelOrder_success() {
            // given
            CancelOrderCommand cancelOrderCommand = createCancelOrderCommand();
            Order order = createOrder();
            given(orderRepository.findById(cancelOrderCommand.orderId())).willReturn(Optional.of(order));

            // when
            orderService.cancelOrder(cancelOrderCommand);

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
        }

        @Test
        @DisplayName("존재하지 않는 주문을 취소하면 예외가 발생한다")
        void cancelOrder_whenOrderNotFound_shouldThrow() {
            // given
            CancelOrderCommand command = createCancelOrderCommand();

            given(orderRepository.findById(command.orderId()))
                .willReturn(Optional.empty());

            // when / then
            assertThatThrownBy(() -> orderService.cancelOrder(command))
                .isInstanceOf(OrderApplicationException.class);
        }

        @Test
        @DisplayName("다른 고객이 주문을 취소하면 예외가 발생한다")
        void cancelOrder_whenNotOwner_shouldThrow() {
            // given
            UUID orderId = UUID.randomUUID();
            CancelOrderCommand command = new CancelOrderCommand(
                OrderActorFactory.from(UUID.randomUUID(), UserRole.COMPANY_MANAGER),
                orderId
            );

            Order order = createOrder();
            given(orderRepository.findById(command.orderId())).willReturn(Optional.of(order));

            // when / then
            assertThatThrownBy(() -> {
                orderService.cancelOrder(command);
            }).isInstanceOf(OrderDomainException.class);
        }
    }


}