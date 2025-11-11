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

    public static void assertCanCancel(Order order, OrderActor actor) {
        assertCanTransition(order, OrderStatus.CANCELED);
        if (!CANCEL_ALLOWED.contains(order.getStatus())) {
            throw new OrderDomainException(OrderDomainErrorCode.INVALID_CANCEL_STATUS);
        }

        if (actor.role() != OrderActorRole.MASTER) {
            assertOwner(order, actor);
        }
    }

    private static void assertOwner(Order order, OrderActor actor) {
        if (!actor.customerId().equals(order.getCustomerId())) {
            throw new OrderDomainException(OrderDomainErrorCode.UNAUTHORIZED_ORDER_ACCESS);
        }
    }

    public static void assertCanDelete(Order order) {
        assertCanTransition(order, OrderStatus.CANCELED);

        if (!DELETE_ALLOWED.contains(order.getStatus())) {
            throw new OrderDomainException(OrderDomainErrorCode.INVALID_CANCEL_STATUS);
        }
    }
}
