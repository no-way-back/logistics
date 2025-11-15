package com.nowayback.order.application;

import static com.nowayback.order.fixture.OrderFixture.createOrderCommand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.nowayback.order.order.application.OrderService;
import com.nowayback.order.order.application.client.DeliveryClient;
import com.nowayback.order.order.application.client.ProductClient;
import com.nowayback.order.order.application.client.response.CreateDeliveryResponse;
import com.nowayback.order.order.application.client.response.DecreaseStockResponse;
import com.nowayback.order.order.application.command.CreateOrderCommand;
import com.nowayback.order.order.application.exception.OrderApplicationException;
import com.nowayback.order.order.domain.entity.Order;
import com.nowayback.order.order.domain.repository.OrderRepository;
import com.nowayback.order.order.domain.vo.OrderStatus;
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
    class createOrder {
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
}