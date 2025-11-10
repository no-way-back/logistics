package com.nowayback.delivery.presentation;

import com.nowayback.delivery.application.DeliveryService;
import com.nowayback.delivery.application.command.CreateDeliveryCommand;
import com.nowayback.delivery.presentation.dto.request.CreateDeliveryRequest;
import com.nowayback.delivery.presentation.dto.response.DeliveryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryResponse createDelivery(
            @RequestBody CreateDeliveryRequest request
    ) {
        CreateDeliveryCommand command = CreateDeliveryCommand.from(request);

        return DeliveryResponse.from(deliveryService.createDelivery(command));
    }
}
