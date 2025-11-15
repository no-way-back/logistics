package com.nowayback.order.order.domain.vo;


import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemSnapshot(
    UUID productId,
    String name,
    BigDecimal price,
    int quantity
) {
}
