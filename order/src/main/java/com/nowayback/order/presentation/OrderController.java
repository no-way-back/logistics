package com.nowayback.order.presentation;

import com.nowayback.common.dto.PageResponse;
import com.nowayback.common.security.annotation.AuthUser;
import com.nowayback.common.security.annotation.CurrentUser;
import com.nowayback.order.application.OrderService;
import com.nowayback.order.application.command.CancelOrderCommand;
import com.nowayback.order.application.command.CreateOrderCommand;
import com.nowayback.order.application.command.GetOrderCommand;
import com.nowayback.order.application.command.GetOrdersCommand;
import com.nowayback.order.application.dto.OrderCreateResult;
import com.nowayback.order.application.dto.OrderResult;
import com.nowayback.order.domain.vo.OrderStatus;
import com.nowayback.order.presentation.request.OrderCreateRequest;
import com.nowayback.order.presentation.request.OrderCreateRequest.OrderItemRequest;
import com.nowayback.order.presentation.response.OrderCreateResponse;
import com.nowayback.order.presentation.response.OrderResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderCreateResponse> createOrder(
        @CurrentUser AuthUser authUser,
        @Valid @RequestBody OrderCreateRequest request
    ) {
        CreateOrderCommand command = CreateOrderCommand.of(
            authUser.userId(),
            authUser.role(),
            request.supplier().supplierCompanyId(),
            request.supplier().name(),
            request.supplier().address(),
            request.supplier().detailAddress(),
            request.supplier().contact(),
            request.receiver().receiverCompanyId(),
            request.receiver().name(),
            request.receiver().address(),
            request.receiver().detailAddress(),
            request.receiver().contact(),
            request.request(),
            request.orderItems().stream()
                .map(OrderItemRequest::toCreateOrderItem)
                .toList()
        );

        OrderCreateResult result = orderService.createOrderWithEvent(command);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(OrderCreateResponse.from(result));
    }

    @PatchMapping("/orders/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
        @CurrentUser AuthUser authUser,
        @PathVariable("orderId") UUID orderId
    ) {
        CancelOrderCommand command = CancelOrderCommand.of(
            authUser.userId(),
            authUser.role(),
            orderId
        );

        orderService.cancelOrder(command);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
        @CurrentUser AuthUser authUser,
        @PathVariable("orderId") UUID orderId
    ) {
        GetOrderCommand command = GetOrderCommand.of(
            authUser.userId(),
            authUser.role(),
            orderId
        );

        OrderResult result = orderService.getOrder(command);

        return ResponseEntity.ok(OrderResponse.from(result));
    }

    @GetMapping("/orders")
    public ResponseEntity<PageResponse<OrderResponse>> getOrders(
        @CurrentUser AuthUser authUser,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) String orderBy,
        @RequestParam(required = false) OrderStatus status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        GetOrdersCommand command = GetOrdersCommand.of(
            authUser.userId(),
            authUser.role(),
            sort,
            orderBy,
            status,
            page,
            size
        );

        Page<OrderResult> orderResultPage = orderService.getOrders(command);

        PageResponse<OrderResponse> response = PageResponse.fromPage(
            orderResultPage.map(OrderResponse::from),
            StringUtils.hasText(orderBy) ? orderBy : "created_at",
            "asc".equalsIgnoreCase(sort)
        );

        return ResponseEntity.ok(response);
    }
}
