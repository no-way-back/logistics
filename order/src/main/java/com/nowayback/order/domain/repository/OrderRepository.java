package com.nowayback.order.domain.repository;

import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.domain.vo.CustomerId;
import com.nowayback.order.domain.vo.OrderStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(UUID orderId);

    Page<Order> searchOrders(CustomerId customerId, OrderStatus status, String sort, String orderBy, PageRequest pageRequest);
}
