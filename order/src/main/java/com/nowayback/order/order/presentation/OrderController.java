package com.nowayback.order.presentation;

import com.nowayback.order.application.OrderService;
import com.nowayback.order.application.dto.OrderCreateResult;
import com.nowayback.order.presentation.request.OrderCreateRequest;
import com.nowayback.order.presentation.response.OrderCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderCreateResponse> createOrder(
        @Valid @RequestBody OrderCreateRequest request
    ) {
        OrderCreateResult result = orderService.createOrder(request.toCreateOrderCommand());

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(OrderCreateResponse.from(result));
    }
}
