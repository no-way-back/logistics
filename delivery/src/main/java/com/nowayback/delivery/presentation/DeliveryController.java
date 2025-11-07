package com.nowayback.delivery.presentation;

import com.nowayback.delivery.application.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
}
