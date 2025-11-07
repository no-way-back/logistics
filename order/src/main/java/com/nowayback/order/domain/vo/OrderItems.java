package com.nowayback.order.domain.vo;

import com.nowayback.order.domain.entity.OrderItem;
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

    public void add(OrderItem item) { orderItems.add(item); }
    public void remove(OrderItem item) { orderItems.remove(item); }

    public BigDecimal getTotalPrice() {
        return orderItems.stream()
            .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getItemsName() {
        int size = orderItems.size();
        if (size == 0) {
            return "";
        }
        if (size == 1) {
            return orderItems.get(0).getName();
        }
        return new StringBuilder()
            .append(orderItems.get(0).getName())
            .append("외 ")
            .append(size - 1)
            .append("건")
            .toString();
    }

    public List<OrderItem> asReadOnly() {
        return Collections.unmodifiableList(orderItems);
    }

    public void validate() {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목은 최소 1개 이상이어야 합니다");
        }

        boolean hasQuantity = orderItems.stream()
            .allMatch(item -> item.getQuantity() > 0);

        if (!hasQuantity) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다");
        }
    }

    public String getOrderName() {
        return getItemsName() + getTotalPrice() + "원";
    }
}
