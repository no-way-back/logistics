package com.nowayback.delivery.presentation.deliverymanager;

import com.nowayback.common.security.annotation.AuthUser;
import com.nowayback.common.security.annotation.CurrentUser;
import com.nowayback.common.security.annotation.RequireRole;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.delivery.application.deliverymanager.DeliveryManagerService;
import com.nowayback.delivery.application.deliverymanager.command.CreateDeliveryManagerCommand;
import com.nowayback.delivery.application.deliverymanager.dto.DeliveryManagerResult;
import com.nowayback.delivery.presentation.deliverymanager.dto.request.CreateDeliveryManagerRequest;
import com.nowayback.delivery.presentation.deliverymanager.dto.response.DeliveryManagerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/delivery-managers")
@RequiredArgsConstructor
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    @PostMapping
    @RequireRole(UserRole.MASTER)
    public ResponseEntity<DeliveryManagerResponse> createDeliveryManager(
            @Valid @RequestBody CreateDeliveryManagerRequest request
    ) {
        CreateDeliveryManagerCommand command = CreateDeliveryManagerCommand.of(
                request.userId(),
                request.hubId(),
                request.type()
        );

        DeliveryManagerResult result = deliveryManagerService.createDeliveryManager(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(DeliveryManagerResponse.from(result));
    }

    @DeleteMapping
    @RequireRole(UserRole.MASTER)
    public ResponseEntity<Void> deleteDeliveryManager(
            @CurrentUser AuthUser authUser,
            @RequestParam UUID deliveryManagerId
    ) {
        deliveryManagerService.deleteDeliveryManager(authUser.userId(), deliveryManagerId);

        return ResponseEntity.noContent().build();
    }
}
