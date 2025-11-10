package com.nowayback.order.presentation.request;

import com.nowayback.order.application.command.CreateOrderCommand;
import com.nowayback.order.application.command.CreateOrderCommand.CreateOrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderCreateRequest(
    @Valid @NotNull(message = "공급업체 정보(supplier)는 필수입니다.")
    SupplierRequest supplier,

    @Valid @NotNull(message = "수령업체 정보(receiver)는 필수입니다.")
    ReceiverRequest receiver,

    String request,

    @Valid
    @NotEmpty(message = "주문 상품(orderItems)은 필수입니다.")
    List<OrderItemRequest> orderItems
) {

    public CreateOrderCommand toCreateOrderCommand() {
        return CreateOrderCommand.of(
            supplier.supplierCompanyId(),
            supplier.name(),
            supplier.address(),
            supplier.detailAddress(),
            supplier.contact(),
            receiver.receiverCompanyId(),
            receiver.name(),
            receiver.address(),
            receiver.detailAddress(),
            receiver.contact(),
            request(),
            orderItems.stream()
                .map(OrderItemRequest::toCreateOrderItem)
                .toList()
        );
    }

    public record SupplierRequest(
        @NotNull(message = "supplierCompanyId는 필수입니다.")
        UUID supplierCompanyId,

        @NotBlank(message = "공급업체 이름(name)은 필수입니다.")
        String name,

        @NotBlank(message = "공급업체 주소(address)는 필수입니다.")
        String address,

        @NotBlank(message = "공급업체 상세주소(detailAddress)는 필수입니다.")
        String detailAddress,

        @NotBlank(message = "공급업체 연락처(contact)는 필수입니다.")
        String contact
    ) {}

    public record ReceiverRequest(
        @NotNull(message = "receiverCompanyId는 필수입니다.")
        UUID receiverCompanyId,

        @NotBlank(message = "수령업체 이름(name)은 필수입니다.")
        String name,

        @NotBlank(message = "수령업체 주소(address)는 필수입니다.")
        String address,

        @NotBlank(message = "수령업체 상세주소(detailAddress)는 필수입니다.")
        String detailAddress,

        @NotBlank(message = "수령업체 연락처(contact)는 필수입니다.")
        String contact
    ) {}

    public record OrderItemRequest(
        @NotNull(message = "productId는 필수입니다.")
        UUID productId,

        @NotBlank(message = "상품명(name)은 필수입니다.")
        String name,

        @NotNull(message = "가격(price)은 필수입니다.")
        @Positive(message = "가격(price)은 0보다 커야 합니다.")
        BigDecimal price,

        @NotNull(message = "수량(quantity)은 필수입니다.")
        @Positive(message = "수량(quantity)은 0보다 커야 합니다.")
        Integer quantity
    ) {
        public CreateOrderItem toCreateOrderItem() {
            return CreateOrderItem.of(productId, name, price, quantity);
        }
    }
}
