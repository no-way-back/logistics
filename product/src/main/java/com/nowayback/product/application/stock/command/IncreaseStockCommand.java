package com.nowayback.product.application.stock.command;

import com.nowayback.product.domain.stock.vo.ProductId;
import com.nowayback.product.domain.stock.vo.Quantity;

import java.util.UUID;

public record IncreaseStockCommand(
        ProductId productId,
        Quantity amount
) {

    public static IncreaseStockCommand of(
            UUID productId,
            int amount
    ) {
        return new IncreaseStockCommand(
                ProductId.of(productId),
                Quantity.of(amount)
        );
    }
}
