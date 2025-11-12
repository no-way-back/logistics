package com.nowayback.order.application.dto;

import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.domain.entity.OrderItem;
import com.nowayback.order.domain.vo.CustomerId;
import com.nowayback.order.domain.vo.OrderStatus;
import com.nowayback.order.domain.vo.ProductId;
import com.nowayback.order.domain.vo.ReceiverCompanyId;
import com.nowayback.order.domain.vo.ReceiverCompanySnapshot;
import com.nowayback.order.domain.vo.SupplierCompanyId;
import com.nowayback.order.domain.vo.SupplierCompanySnapshot;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResult(
    UUID orderId,
    CustomerId customerId,
    SupplierCompanyId supplierCompanyId,
    SupplierCompanySnapshot supplierCompanySnapshot,
    ReceiverCompanyId receiverCompanyId,
    ReceiverCompanySnapshot receiverCompanySnapshot,
    String name,
    String request,
    BigDecimal totalPrice,
    OrderStatus status,
    List<OrderItemResult> orderItems
) {

    public static OrderResult of(Order order) {
        return new OrderResult(
            order.getId(),
            order.getCustomerId(),
            order.getSupplierCompanyId(),
            order.getSupplierCompanySnapshot(),
            order.getReceiverCompanyId(),
            order.getReceiverCompanySnapshot(),
            order.getName(),
            order.getRequest(),
            order.getTotalPrice(),
            order.getStatus(),
            order.getOrderItems().stream()
                .map(OrderItemResult::of)
                .toList()
        );
    }

    public record OrderItemResult(
        ProductId productId,
        String name,
        BigDecimal price,
        Integer quantity
    ) {
        public static OrderItemResult of(OrderItem orderItem) {
            return new OrderItemResult(
                orderItem.getProductId(),
                orderItem.getName(),
                orderItem.getPrice(),
                orderItem.getQuantity()
            );
        }
    }
}
