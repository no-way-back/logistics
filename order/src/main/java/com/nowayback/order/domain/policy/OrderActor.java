package com.nowayback.order.domain.policy;

import com.nowayback.order.domain.vo.CustomerId;

public record OrderActor(
    CustomerId customerId,
    OrderActorRole role
) {

    public static OrderActor from(CustomerId customerId, OrderActorRole role) {
        return new OrderActor(customerId, role);
    }
}
