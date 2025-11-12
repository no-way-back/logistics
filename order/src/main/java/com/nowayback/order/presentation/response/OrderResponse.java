package com.nowayback.order.presentation.response;

import com.nowayback.order.application.dto.OrderResult;
import com.nowayback.order.domain.vo.ReceiverCompanySnapshot;
import com.nowayback.order.domain.vo.SupplierCompanySnapshot;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
    UUID orderId,
    UUID customerId,
    String status,
    String name,
    String request,
    BigDecimal totalPrice,
    SupplierResponse supplier,
    ReceiverResponse receiver,
    List<OrderItemResponse> orderItems
) {

    public static OrderResponse from(OrderResult result) {
        return new OrderResponse(
            result.orderId(),
            result.customerId().getId(),
            result.status().name(),
            result.name(),
            result.request(),
            result.totalPrice(),
            SupplierResponse.from(
                result.supplierCompanyId().getId(),
                result.supplierCompanySnapshot()
            ),
            ReceiverResponse.from(
                result.receiverCompanyId().getId(),
                result.receiverCompanySnapshot()
            ),
            result.orderItems().stream()
                .map(OrderItemResponse::from)
                .toList()
        );
    }

    public record SupplierResponse(
        UUID supplierCompanyId,
        String name,
        String address,
        String detailAddress,
        String contact
    ) {
        public static SupplierResponse from(
            UUID supplierCompanyId,
            SupplierCompanySnapshot snapshot
        ) {
            return new SupplierResponse(
                supplierCompanyId,
                snapshot.getName(),
                snapshot.getAddress(),
                snapshot.getDetailAddress(),
                snapshot.getContact()
            );
        }
    }

    public record ReceiverResponse(
        UUID receiverCompanyId,
        String name,
        String address,
        String detailAddress,
        String contact
    ) {
        public static ReceiverResponse from(
            UUID receiverCompanyId,
            ReceiverCompanySnapshot snapshot
        ) {
            return new ReceiverResponse(
                receiverCompanyId,
                snapshot.getName(),
                snapshot.getAddress(),
                snapshot.getDetailAddress(),
                snapshot.getContact()
            );
        }
    }

    public record OrderItemResponse(
        UUID productId,
        String name,
        BigDecimal price,
        Integer quantity
    ) {
        public static OrderItemResponse from(OrderResult.OrderItemResult itemResult) {
            return new OrderItemResponse(
                itemResult.productId().getId(),
                itemResult.name(),
                itemResult.price(),
                itemResult.quantity()
            );
        }
    }
}
