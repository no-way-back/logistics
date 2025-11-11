package com.nowayback.order.application.command;

import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.order.application.actor.OrderActorFactory;
import com.nowayback.order.domain.policy.OrderActor;
import java.util.UUID;

public record CancelOrderCommand(
    OrderActor actor,
    UUID orderId
) {
    public static CancelOrderCommand of(UUID customerId, UserRole customerRole, UUID orderId) {
        return new CancelOrderCommand(OrderActorFactory.from(customerId, customerRole), orderId);
    }
}
