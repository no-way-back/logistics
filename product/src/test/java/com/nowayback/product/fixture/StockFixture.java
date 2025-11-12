package com.nowayback.product.fixture;

import com.nowayback.product.application.stock.command.DecreaseStockCommand;
import com.nowayback.product.application.stock.command.IncreaseStockCommand;
import com.nowayback.product.domain.stock.entity.Stock;
import com.nowayback.product.domain.stock.vo.ProductId;
import com.nowayback.product.domain.stock.vo.Quantity;

import java.util.UUID;

public class StockFixture {

    public static final UUID STOCK_ID = UUID.randomUUID();

    public static final UUID PRODUCT_UUID = UUID.randomUUID();
    public static final int QUANTITY_VALUE = 100;

    public static final ProductId PRODUCT_ID = ProductId.of(PRODUCT_UUID);
    public static final Quantity QUANTITY = Quantity.of(QUANTITY_VALUE);

    /* stock entity */
    public static Stock createStock() {
        return Stock.create(PRODUCT_ID);
    }

    public static Stock createStock(Quantity quantity) {
        Stock stock = createStock();
        stock.updateQuantity(quantity);
        return stock;
    }

    /* stock command */
    public static final IncreaseStockCommand INCREASE_STOCK_COMMAND = IncreaseStockCommand.of(
            PRODUCT_UUID,
            QUANTITY_VALUE
    );

    public static final DecreaseStockCommand DECREASE_STOCK_COMMAND = DecreaseStockCommand.of(
            PRODUCT_UUID,
            QUANTITY_VALUE
    );

    public static final DecreaseStockCommand INSUFFICIENT_DECREASE_STOCK_COMMAND = DecreaseStockCommand.of(
            PRODUCT_UUID,
            QUANTITY_VALUE + 10
    );
}
