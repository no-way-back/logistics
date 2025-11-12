package com.nowayback.product.domain.stock.vo;

import com.nowayback.product.domain.stock.exception.StockDomainErrorCode;
import com.nowayback.product.domain.stock.exception.StockDomainException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductId {

    private UUID id;

    private ProductId(UUID id) {
        this.id = id;
    }

    public static ProductId of(UUID id) {
        if (id == null) {
            throw new StockDomainException(StockDomainErrorCode.NULL_PRODUCT_ID_VALUE);
        }
        return new ProductId(id);
    }
}
