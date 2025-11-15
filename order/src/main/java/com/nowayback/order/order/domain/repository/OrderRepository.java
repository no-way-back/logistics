package com.nowayback.order.order.domain.repository;

import com.nowayback.order.order.domain.entity.Order;

public interface OrderRepository {
    Order save(Order order);
}
