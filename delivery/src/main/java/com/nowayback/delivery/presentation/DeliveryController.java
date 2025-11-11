package com.nowayback.delivery.presentation;

import com.nowayback.common.security.annotation.AuthUser;
import com.nowayback.common.security.annotation.CurrentUser;
import com.nowayback.common.security.annotation.RequireRole;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.delivery.application.DeliveryService;
import com.nowayback.delivery.application.command.CreateDeliveryCommand;
import com.nowayback.delivery.application.command.UpdateDeliveryRecipientInfoCommand;
import com.nowayback.delivery.application.command.UpdateDeliveryStatusCommand;
import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;
import com.nowayback.delivery.presentation.dto.request.CreateDeliveryRequest;
import com.nowayback.delivery.presentation.dto.request.UpdateDeliveryRecipientInfoRequest;
import com.nowayback.delivery.presentation.dto.request.UpdateDeliveryStatusRequest;
import com.nowayback.delivery.presentation.dto.response.DeliveryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    @RequireRole(UserRole.MASTER)
    public ResponseEntity<DeliveryResponse> createDelivery(
            @Valid @RequestBody CreateDeliveryRequest request,
            @CurrentUser AuthUser authUser
    ) {
        CreateDeliveryCommand command = CreateDeliveryCommand.from(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(DeliveryResponse.from(deliveryService.createDelivery(command)));
    }

    @GetMapping("/{deliveryId}")
    @RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.DELIVERY_MANAGER, UserRole.COMPANY_MANAGER})
    public ResponseEntity<DeliveryResponse> getDelivery(
            @PathVariable UUID deliveryId,
            @CurrentUser AuthUser authUser
    ) {
        return ResponseEntity.ok(
                DeliveryResponse.from(deliveryService.getDelivery(deliveryId))
        );
    }

    @GetMapping
    @RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.DELIVERY_MANAGER, UserRole.COMPANY_MANAGER})
    public ResponseEntity<Page<DeliveryResponse>> searchDeliveries(
            @RequestParam(required = false) UUID orderId,
            @RequestParam(required = false) UUID sourceHubId,
            @RequestParam(required = false) UUID destinationHubId,
            @RequestParam(required = false) DeliveryStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @CurrentUser AuthUser authUser
    ) {
        Page<DeliveryResponse> responses = deliveryService.searchDeliveries(orderId, sourceHubId, destinationHubId, status, page, size)
                .map(DeliveryResponse::from);

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{deliveryId}")
    @RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.DELIVERY_MANAGER})
    public ResponseEntity<DeliveryResponse> updateDelivery(
            @PathVariable UUID deliveryId,
            @Valid @RequestBody UpdateDeliveryRecipientInfoRequest request,
            @CurrentUser AuthUser authUser
    ) {
        UpdateDeliveryRecipientInfoCommand command = UpdateDeliveryRecipientInfoCommand.from(request);

        return ResponseEntity.ok(
                DeliveryResponse.from(deliveryService.updateRecipientInfo(deliveryId, command))
        );
    }

    @PatchMapping("/{deliveryId}/status")
    @RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.DELIVERY_MANAGER})
    public ResponseEntity<DeliveryResponse> updateDeliveryStatus(
            @PathVariable UUID deliveryId,
            @Valid @RequestBody UpdateDeliveryStatusRequest request,
            @CurrentUser AuthUser authUser
    ) {
        UpdateDeliveryStatusCommand command = UpdateDeliveryStatusCommand.from(request);

        return ResponseEntity.ok(
                DeliveryResponse.from(deliveryService.updateDeliveryStatus(deliveryId, command))
        );
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<Void> deleteDelivery(
            @PathVariable UUID deliveryId
    ) {
        deliveryService.deleteDelivery(deliveryId);

        return ResponseEntity.noContent().build();
    }
}
