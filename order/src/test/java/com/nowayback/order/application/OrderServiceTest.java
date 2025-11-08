package com.nowayback.order.application;

import static com.nowayback.order.fixture.OrderFixture.createOrderCommand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.nowayback.order.application.client.DeliveryClient;
import com.nowayback.order.application.client.ProductClient;
import com.nowayback.order.application.client.response.CreateDeliveryResponse;
import com.nowayback.order.application.client.response.DecreaseStockResponse;
import com.nowayback.order.application.command.CreateOrderCommand;
import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.domain.repository.OrderRepository;
import com.nowayback.order.domain.vo.OrderStatus;
import org.junit.jupiter.api.DisplayName;
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
}