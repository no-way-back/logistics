package com.nowayback.order.payment.application;

import com.nowayback.order.order.domain.entity.OrderItem;
import com.nowayback.order.order.domain.vo.ReceiverCompanyId;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentService {

    public void processPayment(UUID orderId, ReceiverCompanyId receiverCompanyId, List<OrderItem> orderItems) {

        // 결제 처리 로직
    }
}
