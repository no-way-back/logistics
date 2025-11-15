package com.nowayback.order.domain.policy;

import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.domain.exception.OrderDomainErrorCode;
import com.nowayback.order.domain.exception.OrderDomainException;
import com.nowayback.order.domain.vo.OrderStatus;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class OrderStatusTransitionPolicy {

    private static final Map<OrderStatus, List<OrderStatus>> TRANSITION_ALLOWED = Map.of(
        OrderStatus.CREATE_PENDING, List.of(OrderStatus.CREATED, OrderStatus.CANCELED),
        OrderStatus.CREATED, List.of(OrderStatus.DELIVERING, OrderStatus.CANCELED),
        OrderStatus.DELIVERING, List.of(OrderStatus.COMPLETED),
        OrderStatus.CANCELED, List.of(),
        OrderStatus.COMPLETED, List.of()
    );

    private static final Set<OrderStatus> CANCEL_ALLOWED = EnumSet.of(
        OrderStatus.CREATE_PENDING, OrderStatus.CREATED);

    private static final Set<OrderStatus> DELETE_ALLOWED = EnumSet.of(
        OrderStatus.CREATE_PENDING, OrderStatus.CREATED);

    public static List<OrderStatus> nextStatuses(OrderStatus current) {
        return TRANSITION_ALLOWED.getOrDefault(current, List.of());
    }

    public static void assertCanTransition(Order order, OrderStatus newStatus) {
        if (!TRANSITION_ALLOWED
            .getOrDefault(
                order.getStatus(),
                List.of())
            .contains(newStatus)) {
            throw new OrderDomainException(OrderDomainErrorCode.INVALID_STATUS_TRANSITION);
        }
    }

    public static void assertCanCancel(Order order, OrderStatus newStatus) {
        assertCanTransition(order, newStatus);

        if (!CANCEL_ALLOWED.contains(order.getStatus())) {
            throw new OrderDomainException(OrderDomainErrorCode.INVALID_CANCEL_STATUS);
        }
    }

    public static void assertCanDelete(Order order, OrderStatus newStatus) {
        assertCanTransition(order, newStatus);

        if (!DELETE_ALLOWED.contains(order.getStatus())) {
            throw new OrderDomainException(OrderDomainErrorCode.INVALID_CANCEL_STATUS);
        }
    }
}
