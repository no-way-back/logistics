package com.nowayback.order.infrastructure.persistence;

import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.domain.repository.OrderRepository;
import com.nowayback.order.domain.vo.CustomerId;
import com.nowayback.order.domain.vo.OrderStatus;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    private final OrderQueryRepository orderQueryRepository;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return orderJpaRepository.findById(orderId);
    }

    @Override
    public Page<Order> searchOrders(
        CustomerId customerId,
        OrderStatus status,
        String sort,
        String orderBy,
        PageRequest pageRequest
    ) {
        return orderQueryRepository.searchOrders(customerId, status, sort, orderBy, pageRequest);
    }
}
