package com.nowayback.order.application.command;

import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.order.application.actor.OrderActorFactory;
import com.nowayback.order.domain.policy.OrderActor;
import java.util.UUID;

public record GetOrderCommand(
    OrderActor actor,
    UUID orderId
) {
    public static GetOrderCommand of(UUID userId, UserRole userRole, UUID orderId) {
        return new GetOrderCommand(
            OrderActorFactory.from(userId, userRole),
            orderId
        );
    }
}
