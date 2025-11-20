package com.nowayback.delivery.infrastructure.delivery;

import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;
import com.nowayback.delivery.domain.delivery.vo.HubId;
import com.nowayback.delivery.domain.delivery.vo.OrderId;
import org.springframework.data.domain.Page;

public interface DeliveryCustomRepository {
    Page<Delivery> searchDeliveries(OrderId orderId, HubId sourceHubId, HubId destinationHubId, DeliveryStatus status, int page, int size);
}
