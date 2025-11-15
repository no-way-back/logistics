package com.nowayback.order.order.application;

import com.nowayback.order.order.application.client.DeliveryClient;
import com.nowayback.order.order.application.client.ProductClient;
import com.nowayback.order.order.application.client.request.CreateDeliveryRequest;
import com.nowayback.order.order.application.client.request.DecreaseStockRequest;
import com.nowayback.order.order.application.client.request.DecreaseStockRequest.DecreaseStockItem;
import com.nowayback.order.order.application.client.response.CreateDeliveryResponse;
import com.nowayback.order.order.application.client.response.DecreaseStockResponse;
import com.nowayback.order.order.application.command.CreateOrderCommand;
import com.nowayback.order.order.application.command.CreateOrderCommand.CreateOrderItem;
import com.nowayback.order.order.application.dto.OrderCreateResult;
import com.nowayback.order.order.application.exception.OrderApplicationErrorCode;
import com.nowayback.order.order.application.exception.OrderApplicationException;
import com.nowayback.order.order.domain.entity.Order;
import com.nowayback.order.order.domain.event.OrderCreatedEvent;
import com.nowayback.order.order.domain.repository.OrderRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;
    private final DeliveryClient deliveryClient;
    private final OrderRepository orderRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public OrderCreateResult createOrder(CreateOrderCommand command) {
        // decreaseStock(command.createOrderItems());

        Order order = command.toEntity();
        Order savedOrder = orderRepository.save(order);

        // createDelivery(savedOrder);

        savedOrder.completeCreation();

        // 이벤트 발행
        applicationEventPublisher.publishEvent(OrderCreatedEvent.of(savedOrder));
        return OrderCreateResult.of(order.getId());
    }

    private void decreaseStock(List<CreateOrderItem> createOrderItems) {
        DecreaseStockResponse decreaseStockResponse = productClient.decreaseStocks(
            DecreaseStockRequest.of(
                createOrderItems.stream()
                    .map(item -> DecreaseStockItem.of(item.productId(), item.quantity()))
                    .toList()
            )
        );

        if (!decreaseStockResponse.success()) {
            throw new OrderApplicationException(OrderApplicationErrorCode.NOT_ENOUGH_STOCK);
        }
    }

    private void createDelivery(Order order) {
        // TODO: hub id 추가 필요
        String fullAddress = order.getReceiverCompanySnapshot().getAddress() + " "
            + order.getReceiverCompanySnapshot().getDetailAddress();

        CreateDeliveryResponse response = deliveryClient.createDelivery(
            CreateDeliveryRequest.of(
                order.getId(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                fullAddress,
                order.getReceiverCompanySnapshot().getName(),
                order.getReceiverCompanySnapshot().getContact()
            )
        );

        if (!response.success()) {
            throw new OrderApplicationException(OrderApplicationErrorCode.DELIVERY_CREATION_FAILED);
        }
    }
}
