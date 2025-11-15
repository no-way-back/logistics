package com.nowayback.order.domain.repository;

import com.nowayback.order.domain.entity.Order;

public interface OrderRepository {
    Order save(Order order);
}
