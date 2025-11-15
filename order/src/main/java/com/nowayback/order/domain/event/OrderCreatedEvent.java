package com.nowayback.order.domain.event;

import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.domain.entity.OrderItem;
import com.nowayback.order.domain.vo.ReceiverCompanyId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCreatedEvent {
    private final UUID orderId;
    private final ReceiverCompanyId receiverCompanyId;
    private final List<OrderItem> orderItems;
    private final LocalDateTime occurredAt; // 이벤트 발생 시점을 기록

    public static OrderCreatedEvent of(Order order) {
        return new OrderCreatedEvent(
            order.getId(),
            order.getReceiverCompanyId(),
            order.getOrderItems(),
            LocalDateTime.now()
        );
    }
}