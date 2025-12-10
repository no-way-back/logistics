package com.nowayback.payment.presentation;

import com.nowayback.common.security.annotation.AuthUser;
import com.nowayback.common.security.annotation.CurrentUser;
import com.nowayback.payment.application.PaymentService;
import com.nowayback.payment.application.dto.command.CancelPaymentCommand;
import com.nowayback.payment.application.dto.command.ConfirmPaymentCommand;
import com.nowayback.payment.presentation.dto.request.CancelPaymentRequest;
import com.nowayback.payment.presentation.dto.request.ConfirmPaymentRequest;
import com.nowayback.payment.presentation.dto.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private PaymentService paymentService;

    @PostMapping("/confirm")
    public PaymentResponse confirmPayment(
            @CurrentUser AuthUser user,
            @RequestBody ConfirmPaymentRequest request
    ) {
        ConfirmPaymentCommand command = ConfirmPaymentCommand.of(
                request.orderId(),
                request.pgMethod(),
                request.pgPaymentKey(),
                request.pgOrderId()
        );

        return PaymentResponse.from(paymentService.confirmPayment(command));
    }

    @PostMapping("/cancel")
    public PaymentResponse cancelPayment(
            @CurrentUser AuthUser user,
            @RequestBody CancelPaymentRequest request
    ) {
        CancelPaymentCommand command = CancelPaymentCommand.of(
                request.paymentId(),
                request.cancelReason()
        );

        return PaymentResponse.from(paymentService.cancelPayment(command));
    }
}
