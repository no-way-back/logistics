package com.nowayback.order.domain.vo;

import static com.nowayback.order.domain.util.DomainPreconditions.nonEmpty;

import com.nowayback.order.domain.entity.OrderItem;
import com.nowayback.order.domain.exception.OrderDomainErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItems {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems = new ArrayList<>();

    public static OrderItems of(List<OrderItem> items) {
        OrderItems orderItems = new OrderItems();
        if (items != null && !items.isEmpty()) {
            orderItems.orderItems.addAll(items);
        }
        return orderItems;
    }

    public void add(OrderItem item) { this.orderItems.add(item); }
    public void remove(OrderItem item) { this.orderItems.remove(item); }

    public BigDecimal getTotalPrice() {
        return this.orderItems.stream()
            .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getItemsName() {
        int size = this.orderItems.size();
        if (size == 0) {
            return "";
        }
        if (size == 1) {
            return this.orderItems.get(0).getName();
        }
        return new StringBuilder()
            .append(this.orderItems.get(0).getName())
            .append("외 ")
            .append(size - 1)
            .append("건")
            .toString();
    }

    public List<OrderItem> asReadOnly() {
        return Collections.unmodifiableList(orderItems);
    }

    public void validate() {
        nonEmpty(this.orderItems, OrderDomainErrorCode.MISSING_ORDER_ITEM_NAME);
    }

    public String getOrderName() {
        return getItemsName() + getTotalPrice() + "원";
    }
}
