package com.nowayback.order.domain.event;

import java.util.UUID;

public record OrderCreatedEvent(
    UUID orderId
) {}
