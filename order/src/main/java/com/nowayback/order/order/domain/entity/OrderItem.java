package com.nowayback.order.order.domain.entity;

import static com.nowayback.order.order.domain.util.DomainPreconditions.hasText;
import static com.nowayback.order.order.domain.util.DomainPreconditions.nonNegative;
import static com.nowayback.order.order.domain.util.DomainPreconditions.notNull;

import com.nowayback.order.order.domain.exception.OrderDomainErrorCode;
import com.nowayback.order.order.domain.vo.ProductId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "product_id", nullable = false, updatable = false))
    private ProductId productId;

    @Column(name= "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    private OrderItem(ProductId productId, String name, BigDecimal price, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItem create(ProductId productId, String name, BigDecimal price, Integer quantity) {
        validateOrderItem(productId, name, price, quantity);
        return new OrderItem(productId, name, price, quantity);
    }

    private static void validateOrderItem(ProductId productId, String name, BigDecimal price, Integer quantity) {
        notNull(productId, OrderDomainErrorCode.NULL_PRODUCT_ID);
        hasText(name, OrderDomainErrorCode.MISSING_ORDER_ITEM_NAME);
        nonNegative(price, OrderDomainErrorCode.INVALID_ORDER_ITEM_PRICE);
        nonNegative(Long.valueOf(quantity), OrderDomainErrorCode.INVALID_ORDER_ITEM_QUANTITY);
    }

}
