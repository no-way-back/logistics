package com.nowayback.order.application.command;

import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.order.application.actor.OrderActorFactory;
import com.nowayback.order.domain.policy.OrderActor;
import com.nowayback.order.domain.vo.OrderStatus;
import java.util.UUID;

public record GetOrdersCommand(
    OrderActor actor,
    String sort,
    String orderBy,
    OrderStatus status,
    int page,
    int size
) {
    public static GetOrdersCommand of(
        UUID userId,
        UserRole userRole,
        String sort,
        String orderBy,
        OrderStatus status,
        int page,
        int size
    ) {

        return new GetOrdersCommand(
            OrderActorFactory.from(userId, userRole),
            sort,
            orderBy,
            status,
            page,
            size
        );
    }
}
