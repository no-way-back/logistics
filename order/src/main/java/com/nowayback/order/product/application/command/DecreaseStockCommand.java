package com.nowayback.order.product.application.command;

import com.nowayback.order.payment.domain.event.PaymentCompletedEvent;
import java.util.List;
import java.util.UUID;

public record DecreaseStockCommand(
    List<StockItem> items
) {

    public static DecreaseStockCommand of(PaymentCompletedEvent event) {
        return new DecreaseStockCommand(
            event.getPaidItems().stream()
                .map(item -> new StockItem(
                    item.productId(),
                    item.quantity()
                ))
                .toList()
        );
    }
    public record StockItem(
        UUID productId,
        int quantity
    ){}
}
