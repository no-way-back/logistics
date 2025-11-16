package com.nowayback.order.order.domain.event;

import com.nowayback.order.common.event.DomainEvent;
import com.nowayback.order.common.eventstore.vo.AggregateType;
import com.nowayback.order.common.eventstore.vo.EventType;
import com.nowayback.order.order.domain.entity.Order;
import com.nowayback.order.order.domain.vo.OrderItemSnapshot;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCreatedEvent implements DomainEvent {
    private final UUID orderId;
    private final UUID receiverCompanyId;
    private final List<OrderItemSnapshot> orderItems;
    private final LocalDateTime occurredAt; // 이벤트 발생 시점을 기록

    public static OrderCreatedEvent of(Order order) {
        return new OrderCreatedEvent(
            order.getId(),
            order.getReceiverCompanyId().getId(),
            order.getOrderItems().stream()
                .map(item -> new OrderItemSnapshot(
                    item.getProductId().getId(),
                    item.getName(),
                    item.getPrice(),
                    item.getQuantity()
                ))
                .toList(),
            LocalDateTime.now()
        );
    }

    @Override
    public UUID getAggregateId() {
        return orderId;
    }

    @Override
    public AggregateType getAggregateType() {
        return AggregateType.ORDER;
    }

    @Override
    public EventType getEventType() {
        return EventType.ORDER_CREATED;
    }
}