package com.nowayback.product.domain.product.vo;

import com.nowayback.product.domain.product.exception.ProductDomainErrorCode;
import com.nowayback.product.domain.product.exception.ProductDomainException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductInfo {

    private String name;
    private int price;

    private ProductInfo(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public static ProductInfo of(String name, int price) {
        validateName(name);
        validatePrice(price);

        return new ProductInfo(name, price);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ProductDomainException(ProductDomainErrorCode.INVALID_PRODUCT_NAME);
        }
    }

    private static void validatePrice(int price) {
        if (price < 0) {
            throw new ProductDomainException(ProductDomainErrorCode.NEGATIVE_PRODUCT_PRICE);
        }
    }
}
