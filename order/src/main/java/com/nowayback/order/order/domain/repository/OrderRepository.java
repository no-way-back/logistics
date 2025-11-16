package com.nowayback.order.order.domain.repository;

import com.nowayback.order.order.domain.entity.Order;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(UUID orderId);
}
