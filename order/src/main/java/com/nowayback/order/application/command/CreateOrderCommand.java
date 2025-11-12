package com.nowayback.order.application.command;

import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.order.application.actor.OrderActorFactory;
import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.domain.entity.OrderItem;
import com.nowayback.order.domain.policy.OrderActor;
import com.nowayback.order.domain.vo.OrderItems;
import com.nowayback.order.domain.vo.ProductId;
import com.nowayback.order.domain.vo.ReceiverCompanyId;
import com.nowayback.order.domain.vo.ReceiverCompanySnapshot;
import com.nowayback.order.domain.vo.SupplierCompanyId;
import com.nowayback.order.domain.vo.SupplierCompanySnapshot;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderCommand(
    OrderActor actor,
    SupplierCompanyId supplierCompanyId,
    SupplierCompanySnapshot supplierCompanySnapshot,
    ReceiverCompanyId receiverCompanyId,
    ReceiverCompanySnapshot receiverCompanySnapshot,
    String request,
    List<CreateOrderItem> createOrderItems
) {

    public static CreateOrderCommand of(
        UUID customerId,
        UserRole customerRole,
        UUID supplierCompanyId,
        String supplierName,
        String supplierAddress,
        String supplierDetailAddress,
        String supplierContact,
        UUID receiverCompanyId,
        String receiverName,
        String receiverAddress,
        String receiverDetailAddress,
        String receiverContact,
        String request,
        List<CreateOrderItem> orderItems
    ) {
        return new CreateOrderCommand(
            OrderActorFactory.from(customerId, customerRole),
            SupplierCompanyId.of(supplierCompanyId),
            SupplierCompanySnapshot.of(
                supplierName,
                supplierAddress,
                supplierDetailAddress,
                supplierContact
            ),
            ReceiverCompanyId.of(receiverCompanyId),
            ReceiverCompanySnapshot.of(
                receiverName,
                receiverAddress,
                receiverDetailAddress,
                receiverContact
            ),
            request,
            orderItems
        );
    }

    public Order toEntity() {
        return Order.create(
            actor.customerId(),
            request,
            supplierCompanyId,
            supplierCompanySnapshot,
            receiverCompanyId,
            receiverCompanySnapshot,
            OrderItems.of(
                createOrderItems.stream()
                    .map(CreateOrderItem::toEntity)
                    .toList()
            )
        );
    }

    public record CreateOrderItem(
        UUID productId,
        String name,
        BigDecimal price,
        Integer quantity
    ) {
        public static CreateOrderItem of(
            UUID productId,
            String name,
            BigDecimal price,
            Integer quantity
        ) {
            return new CreateOrderItem(productId, name, price, quantity);
        }

        public OrderItem toEntity() {
            return OrderItem.create(
                ProductId.of(productId),
                name,
                price,
                quantity
            );
        }
    }
}
