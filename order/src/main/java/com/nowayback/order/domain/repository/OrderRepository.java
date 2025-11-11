package com.nowayback.order.domain.repository;

import com.nowayback.order.domain.entity.Order;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(UUID orderId);
}
