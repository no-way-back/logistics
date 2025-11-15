package com.nowayback.order.order.infrastructure.persistence;

import com.nowayback.order.order.domain.entity.Order;
import com.nowayback.order.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }
}
