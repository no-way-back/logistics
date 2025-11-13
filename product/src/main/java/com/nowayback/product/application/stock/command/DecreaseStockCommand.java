package com.nowayback.product.application.stock.command;

import com.nowayback.product.domain.stock.vo.ProductId;
import com.nowayback.product.domain.stock.vo.Quantity;

import java.util.UUID;

public record DecreaseStockCommand (
        ProductId productId,
        Quantity amount
) {

    public static DecreaseStockCommand of(
            UUID productId,
            int amount
    ) {
        return new DecreaseStockCommand(
                ProductId.of(productId),
                Quantity.of(amount)
        );
    }
}