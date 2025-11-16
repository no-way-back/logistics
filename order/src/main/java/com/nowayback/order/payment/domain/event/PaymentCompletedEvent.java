package com.nowayback.order.payment.domain.event;

import com.nowayback.order.common.event.DomainEvent;
import com.nowayback.order.common.eventstore.vo.AggregateType;
import com.nowayback.order.common.eventstore.vo.EventType;
import com.nowayback.order.order.domain.vo.OrderItemSnapshot;
import com.nowayback.order.payment.domain.entity.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentCompletedEvent implements DomainEvent {

    private final UUID paymentId;
    private final UUID orderId;
    private final List<PaidItem> paidItems;
    private final LocalDateTime occurredAt; // 이벤트 발생 시점을 기록

    public static PaymentCompletedEvent of(Payment payment, List<OrderItemSnapshot> orderItems) {
        return new PaymentCompletedEvent(
            payment.getId(),
            payment.getOrderId(),
            orderItems.stream()
                .map(item -> new PaidItem(
                    item.productId(),
                    item.name(),
                    item.price(),
                    item.quantity()
                ))
                .toList(),
            LocalDateTime.now()
        );
    }

    @Override
    public UUID getAggregateId() {
        return paymentId;
    }

    @Override
    public AggregateType getAggregateType() {
        return AggregateType.PAYMENT;
    }

    @Override
    public EventType getEventType() {
        return EventType.PAYMENT_COMPLETED;
    }

    public record PaidItem(
        UUID productId,
        String name,
        BigDecimal price,
        int quantity
    ) {}
}
