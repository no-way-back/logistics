package com.nowayback.order.product.application.eventlistener;

import com.nowayback.order.payment.domain.event.PaymentCompletedEvent;
import com.nowayback.order.product.application.ProductService;
import com.nowayback.order.product.application.command.DecreaseStockCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventListener {
    private final ProductService productService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("결제 완료 이벤트 수신 - 재고 차감 시작");

        // 재고 차감
        productService.decreaseStock(
            DecreaseStockCommand.of(event)
        );

        log.info("재고 차감 완료");
    }
}
