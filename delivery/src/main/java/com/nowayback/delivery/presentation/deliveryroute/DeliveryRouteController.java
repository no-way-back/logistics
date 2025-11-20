package com.nowayback.delivery.presentation.deliveryroute;

import com.nowayback.common.dto.PageResponse;
import com.nowayback.common.security.annotation.AuthUser;
import com.nowayback.common.security.annotation.CurrentUser;
import com.nowayback.common.security.annotation.RequireRole;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.delivery.application.deliveryroute.DeliveryRouteService;
import com.nowayback.delivery.application.deliveryroute.command.UpdateDeliveryRouteInfoCommand;
import com.nowayback.delivery.application.deliveryroute.command.UpdateDeliveryRouteStatusCommand;
import com.nowayback.delivery.application.deliveryroute.dto.DeliveryRouteResult;
import com.nowayback.delivery.presentation.deliveryroute.dto.request.UpdateDeliveryRouteInfoRequest;
import com.nowayback.delivery.presentation.deliveryroute.dto.request.UpdateDeliveryRouteStatusRequest;
import com.nowayback.delivery.presentation.deliveryroute.dto.response.DeliveryRouteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/delivery-routes")
@RequiredArgsConstructor
public class DeliveryRouteController {

    private final DeliveryRouteService deliveryRouteService;

    @GetMapping("/{deliveryRouteId}")
    @RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.DELIVERY_MANAGER, UserRole.COMPANY_MANAGER})
    public ResponseEntity<DeliveryRouteResponse> getDeliveryRoute(
            @CurrentUser AuthUser user,
            @PathVariable UUID deliveryRouteId
    ) {
        return ResponseEntity.ok(
                DeliveryRouteResponse.from(deliveryRouteService.getDelivery(user.userId(), user.role(), deliveryRouteId))
        );
    }

    @GetMapping
    @RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.DELIVERY_MANAGER, UserRole.COMPANY_MANAGER})
    public ResponseEntity<PageResponse<DeliveryRouteResponse>> searchDeliveryRoutes(
            @CurrentUser AuthUser user,
            @RequestParam(required = false) UUID deliveryId,
            @PageableDefault(page = 0, size = 10, sort = "sequence", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<DeliveryRouteResponse> responses = deliveryRouteService
                .searchDeliveryRoutes(user.userId(), user.role(), deliveryId, pageable)
                .map(DeliveryRouteResponse::from);

        return ResponseEntity.ok(PageResponse.fromPage(responses));
    }

    @PatchMapping("/{deliveryRouteId}/status")
    @RequireRole({UserRole.MASTER, UserRole.DELIVERY_MANAGER})
    public ResponseEntity<DeliveryRouteResponse> updateDeliveryRouteStatus(
            @CurrentUser AuthUser user,
            @PathVariable UUID deliveryRouteId,
            @Valid @RequestBody UpdateDeliveryRouteStatusRequest request
    ) {
        UpdateDeliveryRouteStatusCommand command = UpdateDeliveryRouteStatusCommand.of(request.deliveryRouteStatus());
        DeliveryRouteResult result = deliveryRouteService.updateDeliveryRouteStatus(user.userId(), user.role(), deliveryRouteId, command);

        return ResponseEntity.ok(DeliveryRouteResponse.from(result));
    }

    @PatchMapping("/{deliveryRouteId}")
    @RequireRole({UserRole.MASTER, UserRole.DELIVERY_MANAGER})
    public ResponseEntity<DeliveryRouteResponse> updateDeliveryRouteInfo(
            @CurrentUser AuthUser user,
            @PathVariable UUID deliveryRouteId,
            @Valid @RequestBody UpdateDeliveryRouteInfoRequest request
    ) {
        UpdateDeliveryRouteInfoCommand command = UpdateDeliveryRouteInfoCommand.of(
                request.actualDistanceMeters(),
                request.actualDurationMinutes()
        );
        DeliveryRouteResult result = deliveryRouteService.updateDeliveryRouteInfo(user.userId(), user.role(), deliveryRouteId, command);

        return ResponseEntity.ok(DeliveryRouteResponse.from(result));
    }
}