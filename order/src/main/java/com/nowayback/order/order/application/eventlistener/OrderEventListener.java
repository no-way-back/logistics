package com.nowayback.order.order.application.eventlistener;


import com.nowayback.order.common.event.AsyncTransactionalEventListener;
import com.nowayback.order.order.domain.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderEventListener extends AsyncTransactionalEventListener<OrderCreatedEvent> {

    @Override
    public void processEvent(OrderCreatedEvent event) {
        log.info("주문 생성 이벤트 수신 - 주문 ID: {}, 상품 ID: {}, ReceiverCompanyId: {}",
            event.getOrderId(),
            event.getOrderItems(),
            event.getReceiverCompanyId()
        );

        // 알림 발송, 로그 저장 등의 작업을 수행
    }
}
