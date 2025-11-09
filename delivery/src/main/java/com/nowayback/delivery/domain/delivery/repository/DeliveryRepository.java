package com.nowayback.delivery.domain.delivery.repository;

import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.vo.OrderId;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);
    boolean existsByOrderId(OrderId orderId);
}
