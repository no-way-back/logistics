package com.nowayback.order.infrastructure.persistence;

import com.nowayback.order.domain.entity.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, UUID> {

}
