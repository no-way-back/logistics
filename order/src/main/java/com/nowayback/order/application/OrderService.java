package com.nowayback.order.application;

import com.nowayback.order.application.client.DeliveryClient;
import com.nowayback.order.application.client.ProductClient;
import com.nowayback.order.application.client.request.CreateDeliveryRequest;
import com.nowayback.order.application.client.request.DecreaseStockRequest;
import com.nowayback.order.application.client.request.DecreaseStockRequest.StockItem;
import com.nowayback.order.application.client.response.CreateDeliveryResponse;
import com.nowayback.order.application.client.response.DecreaseStockResponse;
import com.nowayback.order.application.command.CreateOrderCommand;
import com.nowayback.order.application.command.CreateOrderCommand.CreateOrderItem;
import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.domain.repository.OrderRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;
    private final DeliveryClient deliveryClient;
    private final OrderRepository orderRepository;

    @Transactional
    public void createOrder(CreateOrderCommand command) {
        decreaseStock(command.createOrderItems());

        Order order = command.toEntity();
        orderRepository.save(order);

        createDelivery(order);

        order.completeCreation();
    }

    private void decreaseStock(List<CreateOrderItem> createOrderItems) {
        DecreaseStockResponse decreaseStockResponse = productClient.decreaseStocks(
            DecreaseStockRequest.of(
                createOrderItems.stream()
                    .map(item -> StockItem.of(item.productId(), item.quantity()))
                    .toList()
            )
        );

        if (!decreaseStockResponse.success()) {
            throw new IllegalStateException("재고 차감 실패");
        }
    }

    private void createDelivery(Order order) {
        CreateDeliveryResponse response = deliveryClient.createDelivery(
            CreateDeliveryRequest.of(
                order.getId(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                order.getReceiverCompanySnapshot().getAddress()
                    + order.getReceiverCompanySnapshot().getDetailAddress(),
                order.getReceiverCompanySnapshot().getName(),
                order.getReceiverCompanySnapshot().getContact()
            )
        );

        if (!response.success()) {
            throw new IllegalStateException("배송 생성 실패");
        }
    }
}
