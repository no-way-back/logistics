package com.nowayback.delivery.application;

import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.delivery.application.command.CreateDeliveryRoutesCommand;
import com.nowayback.delivery.application.command.UpdateDeliveryRouteInfoCommand;
import com.nowayback.delivery.application.command.UpdateDeliveryRouteStatusCommand;
import com.nowayback.delivery.application.dto.DeliveryRouteResult;
import com.nowayback.delivery.application.exception.DeliveryRouteApplicationErrorCode;
import com.nowayback.delivery.application.exception.DeliveryRouteApplicationException;
import com.nowayback.delivery.domain.deliveryroute.entity.DeliveryRoute;
import com.nowayback.delivery.domain.deliveryroute.repository.DeliveryRouteRepository;
import com.nowayback.delivery.domain.deliveryroute.vo.DeliveryId;
import com.nowayback.delivery.domain.deliveryroute.vo.DeliveryManagerId;
import com.nowayback.delivery.domain.deliveryroute.vo.RouteInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryRouteService {

    private final DeliveryRouteRepository deliveryRouteRepository;

    @Transactional
    public List<DeliveryRouteResult> createDeliveryRoutes(CreateDeliveryRoutesCommand command) {
        DeliveryId deliveryId = command.deliveryId();

        List<DeliveryRoute> deliveryRoutes = command.segments().stream()
                .map(segment -> createDeliveryRoute(deliveryId, segment))
                .toList();

        List<DeliveryRoute> savedRoutes = deliveryRouteRepository.saveAll(deliveryRoutes);

        return savedRoutes.stream()
                .map(DeliveryRouteResult::from)
                .toList();
    }

    private DeliveryRoute createDeliveryRoute(DeliveryId deliveryId, CreateDeliveryRoutesCommand.DeliveryRouteSegment segment) {
        // TODO: Delivery Manager 할당 로직 추가 필요
        DeliveryManagerId deliveryManagerId =DeliveryManagerId.of(UUID.randomUUID());

        RouteInfo routeInfo = RouteInfo.of(
                segment.expectedDistanceMeters(),
                segment.expectedDurationMinutes(),
                null,
                null
        );

        return DeliveryRoute.create(
                deliveryId,
                segment.sequence(),
                segment.hubRoute(),
                deliveryManagerId,
                routeInfo
        );
    }

    @Transactional(readOnly = true)
    public DeliveryRouteResult getDelivery(UUID userId, UserRole role, UUID deliveryRouteId) {
        DeliveryRoute deliveryRoute = getDeliveryRouteById(deliveryRouteId);
        return DeliveryRouteResult.from(deliveryRoute);
    }

    @Transactional(readOnly = true)
    public Page<DeliveryRouteResult> searchDeliveryRoutes(UUID userId, UserRole role, UUID deliveryId, Pageable pageable) {
        Page<DeliveryRoute> deliveryRoutes = deliveryRouteRepository.findAllByDeliveryId(DeliveryId.of(deliveryId), pageable);
        return deliveryRoutes.map(DeliveryRouteResult::from);
    }

    @Transactional
    public DeliveryRouteResult updateDeliveryRouteStatus(UUID userId, UserRole role, UUID deliveryRouteId, UpdateDeliveryRouteStatusCommand command) {
        DeliveryRoute deliveryRoute = getDeliveryRouteById(deliveryRouteId);

        deliveryRoute.updateStatus(command.status());
        return DeliveryRouteResult.from(deliveryRoute);
    }

    @Transactional
    public DeliveryRouteResult updateDeliveryRouteInfo(UUID userId, UserRole role, UUID deliveryRouteId, UpdateDeliveryRouteInfoCommand command) {
        DeliveryRoute deliveryRoute = getDeliveryRouteById(deliveryRouteId);
        RouteInfo oldRouteInfo = deliveryRoute.getRouteInfo();

        RouteInfo routeInfo = RouteInfo.of(
                oldRouteInfo.getExpectedDistanceMeters(),
                oldRouteInfo.getExpectedDurationMinutes(),
                command.actualDistanceMeters(),
                command.actualDurationMinutes()
        );

        deliveryRoute.updateRouteInfo(routeInfo);
        return DeliveryRouteResult.from(deliveryRoute);
    }

    @Transactional
    public void deleteDeliveryRoutesByDeliveryId(UUID userId, UUID deliveryId) {
        DeliveryRoute deliveryRoute = getDeliveryRouteById(deliveryId);
        deliveryRoute.delete(userId);
    }

    private DeliveryRoute getDeliveryRouteById(UUID deliveryRouteId) {
        return deliveryRouteRepository.findById(deliveryRouteId)
                .orElseThrow(() -> new DeliveryRouteApplicationException(DeliveryRouteApplicationErrorCode.NOT_FOUND_DELIVERY_ROUTE));
    }
}
