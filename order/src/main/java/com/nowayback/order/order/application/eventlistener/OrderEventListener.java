package com.nowayback.order.application.eventlistener;


import com.nowayback.order.domain.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderEventListener {
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("주문 생성 이벤트 수신 - 주문 ID: {}, 상품 ID: {}, ReceiverCompanyId: {}",
            event.getOrderId(),
            event.getOrderItems(),
            event.getReceiverCompanyId()
        );

        // 알림 발송, 로그 저장 등의 작업을 수행
    }
}
