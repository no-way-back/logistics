package com.nowayback.order.application;

import com.nowayback.order.application.client.DeliveryClient;
import com.nowayback.order.application.client.ProductClient;
import com.nowayback.order.application.client.request.CreateDeliveryRequest;
import com.nowayback.order.application.client.request.DecreaseStockRequest;
import com.nowayback.order.application.client.request.DecreaseStockRequest.DecreaseStockItem;
import com.nowayback.order.application.client.response.CreateDeliveryResponse;
import com.nowayback.order.application.client.response.DecreaseStockResponse;
import com.nowayback.order.application.command.CancelOrderCommand;
import com.nowayback.order.application.command.CreateOrderCommand;
import com.nowayback.order.application.command.CreateOrderCommand.CreateOrderItem;
import com.nowayback.order.application.command.GetOrderCommand;
import com.nowayback.order.application.command.GetOrdersCommand;
import com.nowayback.order.application.dto.OrderCreateResult;
import com.nowayback.order.application.dto.OrderResult;
import com.nowayback.order.application.event.publisher.EventPublisher;
import com.nowayback.order.application.exception.OrderApplicationErrorCode;
import com.nowayback.order.application.exception.OrderApplicationException;
import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.application.event.OrderCreatedEvent;
import com.nowayback.order.application.event.payload.OrderCreatedEventPayload;
import com.nowayback.order.application.event.payload.OrderCreatedEventPayload.OrderItemLine;
import com.nowayback.order.domain.policy.OrderActor;
import com.nowayback.order.domain.policy.OrderActorRole;
import com.nowayback.order.domain.repository.OrderRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;
    private final DeliveryClient deliveryClient;
    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public OrderCreateResult createOrder(CreateOrderCommand command) {
        decreaseStock(command.createOrderItems());

        Order order = command.toEntity();
        orderRepository.save(order);

        createDelivery(order);

        order.completeCreation();

        return OrderCreateResult.of(order.getId());
    }

    @Transactional
    public OrderCreateResult createOrderWithEvent(CreateOrderCommand command) {
        Order order = command.toEntity();
        orderRepository.save(order);
        log.info("[OrderService.createOrderWithEvent] order = {}", order);

        publishOrderCreatedEvent(order);

        return OrderCreateResult.of(order.getId());
    }

    @Transactional
    public void cancelOrder(CancelOrderCommand command) {
        Order order = findOrderOrThrow(command.orderId());

        order.cancel(command.actor());
    }

    @Transactional(readOnly = true)
    public OrderResult getOrder(GetOrderCommand command) {
        Order order = findOrderOrThrow(command.orderId());

        assertReadable(order, command.actor());

        return OrderResult.of(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderResult> getOrders(GetOrdersCommand command) {
        OrderActor actor = command.actor();

        boolean isMaster = actor.role().isMaster();

        PageRequest pageRequest = PageRequest.of(command.page(), command.size());
        Page<Order> orders = orderRepository.searchOrders(
            isMaster ? null : actor.customerId(),
            command.status(),
            command.sort(),
            command.orderBy(),
            pageRequest
        );

        return orders.map(OrderResult::of);
    }

    @Transactional
    public void failedDecreaseStock(UUID orderId) {
        Order order = findOrderOrThrow(orderId);

        order.failStockDecrease();
    }

    private void assertReadable(Order order, OrderActor actor) {
        if (actor.role() == OrderActorRole.MASTER) {
            return;
        }

        if (!actor.customerId().getId().equals(order.getCustomerId().getId())) {
            throw new OrderApplicationException(
                OrderApplicationErrorCode.UNAUTHORIZED_ORDER_ACCESS
            );
        }
    }

    /**
     * product-service에 재고 차감 요청
     *
     * @param createOrderItems
     */
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

    /**
     * delivery-service에 배송 생성 요청
     *
     * @param order
     */
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

    private Order findOrderOrThrow(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> {
                throw new OrderApplicationException(OrderApplicationErrorCode.ORDER_NOT_FOUND);
            }
        );
    }

    private void publishOrderCreatedEvent(Order order) {
        eventPublisher.publish(
            OrderCreatedEvent.of(
                order.getId(),
                new OrderCreatedEventPayload(
                    order.getId(),
                    order.getOrderItems().stream()
                        .map(item -> new OrderItemLine(item.getProductId().getId(), item.getQuantity()))
                        .toList()
                )
            )
        );
    }
}
