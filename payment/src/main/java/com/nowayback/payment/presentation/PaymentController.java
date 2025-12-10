package com.nowayback.payment.presentation;

import com.nowayback.payment.application.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private PaymentService paymentService;
}
