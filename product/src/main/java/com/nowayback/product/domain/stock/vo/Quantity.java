package com.nowayback.product.domain.stock.vo;

import com.nowayback.product.domain.stock.exception.StockDomainErrorCode;
import com.nowayback.product.domain.stock.exception.StockDomainException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quantity {

    private int quantity;

    private Quantity(int quantity) {
        this.quantity = quantity;
    }

    public static Quantity of(int quantity) {
        validateQuantity(quantity);

        return new Quantity(quantity);
    }

    public static Quantity zero() {
        return new Quantity(0);
    }

    public Quantity add(Quantity other) {
        validateQuantityObject(other);

        return new Quantity(this.quantity + other.quantity);
    }

    public Quantity subtract(Quantity other) {
        validateQuantityObject(other);

        int resultQuantity = this.quantity - other.quantity;
        if (resultQuantity < 0) {
            throw new StockDomainException(StockDomainErrorCode.INSUFFICIENT_STOCK_QUANTITY);
        }

        return new Quantity(resultQuantity);
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new StockDomainException(StockDomainErrorCode.NEGATIVE_STOCK_QUANTITY);
        }
    }

    private static void validateQuantityObject(Quantity quantity) {
        if (quantity == null) {
            throw new StockDomainException(StockDomainErrorCode.NULL_QUANTITY_OBJECT);
        }
    }
}
