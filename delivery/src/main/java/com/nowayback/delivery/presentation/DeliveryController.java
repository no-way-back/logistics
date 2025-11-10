package com.nowayback.delivery.presentation;

import com.nowayback.delivery.application.DeliveryService;
import com.nowayback.delivery.application.command.CreateDeliveryCommand;
import com.nowayback.delivery.application.command.UpdateDeliveryRecipientInfoCommand;
import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;
import com.nowayback.delivery.presentation.dto.request.CreateDeliveryRequest;
import com.nowayback.delivery.presentation.dto.request.UpdateDeliveryRecipientInfoRequest;
import com.nowayback.delivery.presentation.dto.response.DeliveryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryResponse createDelivery(
            @Valid @RequestBody CreateDeliveryRequest request
    ) {
        CreateDeliveryCommand command = CreateDeliveryCommand.from(request);

        return DeliveryResponse.from(deliveryService.createDelivery(command));
    }

    @GetMapping("/{deliveryId}")
    public DeliveryResponse getDelivery(
            @PathVariable UUID deliveryId
    ) {
        return DeliveryResponse.from(deliveryService.getDelivery(deliveryId));
    }

    @GetMapping
    public Page<DeliveryResponse> searchDeliveries(
            @RequestParam(required = false) UUID orderId,
            @RequestParam(required = false) UUID sourceHubId,
            @RequestParam(required = false) UUID destinationHubId,
            @RequestParam(required = false) DeliveryStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return deliveryService.searchDeliveries(orderId, sourceHubId, destinationHubId, status, page, size)
                .map(DeliveryResponse::from);
    }

    @PatchMapping("/{deliveryId}")
    public DeliveryResponse updateDelivery(
            @PathVariable UUID deliveryId,
            @Valid @RequestBody UpdateDeliveryRecipientInfoRequest request
    ) {
        UpdateDeliveryRecipientInfoCommand command = UpdateDeliveryRecipientInfoCommand.from(request);
        return DeliveryResponse.from(deliveryService.updateRecipientInfo(deliveryId, command));
    }
}
