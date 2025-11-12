package com.nowayback.order.application.actor;

import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.order.domain.policy.OrderActor;
import com.nowayback.order.domain.policy.OrderActorRole;
import com.nowayback.order.domain.vo.CustomerId;
import java.util.UUID;

public class OrderActorFactory {
    public static OrderActor from(UUID customerId, UserRole userRole) {
        return new OrderActor(
            CustomerId.of(customerId),
            mapToOrderActorRole(userRole)
        );
    }

    private static OrderActorRole mapToOrderActorRole(UserRole userRole) {
        return switch (userRole) {
            case MASTER -> OrderActorRole.MASTER;
            case HUB_MANAGER -> OrderActorRole.HUB_MANAGER;
            case DELIVERY_MANAGER -> OrderActorRole.DELIVERY_MANAGER;
            case COMPANY_MANAGER -> OrderActorRole.COMPANY_MANAGER;
        };
    }
}
