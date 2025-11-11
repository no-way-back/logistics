package com.nowayback.delivery.presentation;

import com.nowayback.common.dto.PageResponse;
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
            @Valid @RequestBody CreateDeliveryRequest request
    ) {
        CreateDeliveryCommand command = CreateDeliveryCommand.of(
                request.orderId(),
                request.sourceHubId(),
                request.destinationHubId(),
                request.deliveryAddress(),
                request.recipientName(),
                request.recipientSlackId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(DeliveryResponse.from(deliveryService.createDelivery(command)));
    }

    @GetMapping("/{deliveryId}")
    @RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.DELIVERY_MANAGER, UserRole.COMPANY_MANAGER})
    public ResponseEntity<DeliveryResponse> getDelivery(
            @CurrentUser AuthUser authUser,
            @PathVariable UUID deliveryId
    ) {
        return ResponseEntity.ok(
                DeliveryResponse.from(deliveryService.getDelivery(deliveryId))
        );
    }

    @GetMapping
    @RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.DELIVERY_MANAGER, UserRole.COMPANY_MANAGER})
    public ResponseEntity<PageResponse<DeliveryResponse>> searchDeliveries(
            @CurrentUser AuthUser authUser,
            @RequestParam(required = false) UUID orderId,
            @RequestParam(required = false) UUID sourceHubId,
            @RequestParam(required = false) UUID destinationHubId,
            @RequestParam(required = false) DeliveryStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<DeliveryResponse> responses = deliveryService.searchDeliveries(orderId, sourceHubId, destinationHubId, status, page, size)
                .map(DeliveryResponse::from);

        return ResponseEntity.ok(PageResponse.fromPage(responses));
    }

    @PatchMapping("/{deliveryId}")
    @RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.DELIVERY_MANAGER})
    public ResponseEntity<DeliveryResponse> updateDelivery(
            @CurrentUser AuthUser authUser,
            @PathVariable UUID deliveryId,
            @Valid @RequestBody UpdateDeliveryRecipientInfoRequest request
    ) {
        UpdateDeliveryRecipientInfoCommand command = UpdateDeliveryRecipientInfoCommand.from(request);

        return ResponseEntity.ok(
                DeliveryResponse.from(deliveryService.updateRecipientInfo(deliveryId, command))
        );
    }

    @PatchMapping("/{deliveryId}/status")
    @RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.DELIVERY_MANAGER})
    public ResponseEntity<DeliveryResponse> updateDeliveryStatus(
            @CurrentUser AuthUser authUser,
            @PathVariable UUID deliveryId,
            @Valid @RequestBody UpdateDeliveryStatusRequest request
    ) {
        UpdateDeliveryStatusCommand command = UpdateDeliveryStatusCommand.from(request);

        return ResponseEntity.ok(
                DeliveryResponse.from(deliveryService.updateDeliveryStatus(deliveryId, command))
        );
    }

    @DeleteMapping("/{deliveryId}")
    @RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER})
    public ResponseEntity<Void> deleteDelivery(
            @CurrentUser AuthUser authUser,
            @PathVariable UUID deliveryId
    ) {
        deliveryService.deleteDelivery(deliveryId);

        return ResponseEntity.noContent().build();
    }
}
