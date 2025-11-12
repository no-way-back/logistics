package com.nowayback.product.domain.stock.entity;

import com.nowayback.common.audit.BaseEntity;
import com.nowayback.product.domain.stock.exception.StockDomainErrorCode;
import com.nowayback.product.domain.stock.exception.StockDomainException;
import com.nowayback.product.domain.stock.vo.ProductId;
import com.nowayback.product.domain.stock.vo.Quantity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_stocks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "stock_id", updatable = false, nullable = false)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "product_id", updatable = false, nullable = false))
    private ProductId productId;

    @Embedded
    @AttributeOverride(name = "quantity", column = @Column(name = "quantity", nullable = false))
    private Quantity quantity;

    public Stock(ProductId productId, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static Stock create(ProductId productId) {
        validateProductId(productId);

        return new Stock(productId, Quantity.zero());
    }

    public void updateQuantity(Quantity newQuantity) {
        validateNotNull(newQuantity, StockDomainErrorCode.NULL_QUANTITY_OBJECT);
        this.quantity = newQuantity;
    }

    public void delete(UUID deletedBy) {
        softDelete(deletedBy);
    }

    private static void validateNotNull(Object object, StockDomainErrorCode errorCode) {
        if (object == null) throw new StockDomainException(errorCode);
    }

    private static void validateProductId(ProductId productId) {
        validateNotNull(productId, StockDomainErrorCode.NULL_PRODUCT_ID_OBJECT);
    }
}
