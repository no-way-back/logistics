package com.nowayback.delivery.domain.deliveryroute.entity;

import com.nowayback.common.audit.BaseEntity;
import com.nowayback.delivery.domain.deliveryroute.vo.*;
import com.nowayback.delivery.domain.deliveryroute.exception.DeliveryRouteDomainErrorCode;
import com.nowayback.delivery.domain.deliveryroute.exception.DeliveryRouteDomainException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_delivery_routes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryRoute extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_route_id", updatable = false, nullable = false)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "delivery_id", updatable = false, nullable = false))
    private DeliveryId deliveryId;

    @Embedded
    @AttributeOverride(name = "sequence", column = @Column(name = "route_sequence", nullable = false))
    private RouteSequence sequence;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "sourceHubId.id", column = @Column(name = "source_hub_id", nullable = false, updatable = false)),
            @AttributeOverride(name = "destinationHubId.id", column = @Column(name = "destination_hub_id", nullable = false, updatable = false))
    })
    private HubRoute hubRoute;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "delivery_manager_id", nullable = false))
    private DeliveryManagerId deliveryManagerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryRouteStatus status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "expectedDistanceMeters", column = @Column(name = "expected_distance_meters")),
            @AttributeOverride(name = "expectedDurationMinutes", column = @Column(name = "expected_duration_minutes")),
            @AttributeOverride(name = "actualDistanceMeters", column = @Column(name = "actual_distance_meters")),
            @AttributeOverride(name = "actualDurationMinutes", column = @Column(name = "actual_duration_minutes"))
    })
    private RouteInfo routeInfo;

    private DeliveryRoute(DeliveryId deliveryId, RouteSequence sequence, HubRoute hubRoute, DeliveryManagerId deliveryManagerId, DeliveryRouteStatus status, RouteInfo routeInfo) {
        this.deliveryId = deliveryId;
        this.sequence = sequence;
        this.hubRoute = hubRoute;
        this.deliveryManagerId = deliveryManagerId;
        this.status = status;
        this.routeInfo = routeInfo;
    }

    public static DeliveryRoute create(DeliveryId deliveryId, RouteSequence sequence, HubRoute hubRoute, DeliveryManagerId deliveryManagerId, RouteInfo routeInfo) {
        validateDeliveryId(deliveryId);
        validateRouteSequence(sequence);
        validateHubRoute(hubRoute);
        validateDeliveryManagerId(deliveryManagerId);
        validateRouteInfo(routeInfo);

        return new DeliveryRoute(
                deliveryId,
                sequence,
                hubRoute,
                deliveryManagerId,
                DeliveryRouteStatus.WAITING_AT_HUB,
                routeInfo
        );
    }

    public void updateStatus(DeliveryRouteStatus newStatus) {
        if (!status.canTransitionTo(newStatus)) {
            throw new DeliveryRouteDomainException(DeliveryRouteDomainErrorCode.INVALID_DELIVERY_ROUTE_STATUS_TRANSITION);
        }
        this.status = newStatus;
    }

    public void updateRouteInfo(RouteInfo newRouteInfo) {
        validateRouteInfo(newRouteInfo);

        if (!status.canUpdateRouteInfo()) {
            throw new DeliveryRouteDomainException(DeliveryRouteDomainErrorCode.INVALID_DELIVERY_ROUTE_STATUS_FOR_UPDATE);
        }

        this.routeInfo = newRouteInfo;
    }

    public void delete(UUID deletedBy) {
        softDelete(deletedBy);
    }

    private static void validateNotNull(Object value, DeliveryRouteDomainErrorCode errorCode) {
        if (value == null) throw new DeliveryRouteDomainException(errorCode);
    }

    private static void validateDeliveryId(DeliveryId deliveryId) {
        validateNotNull(deliveryId, DeliveryRouteDomainErrorCode.NULL_DELIVERY_ID_OBJECT);
    }

    private static void validateRouteSequence(RouteSequence sequence) {
        validateNotNull(sequence, DeliveryRouteDomainErrorCode.NULL_ROUTE_SEQUENCE_OBJECT);
    }

    private static void validateHubRoute(HubRoute hubRoute) {
        validateNotNull(hubRoute, DeliveryRouteDomainErrorCode.NULL_HUB_ROUTE_OBJECT);
    }

    private static void validateDeliveryManagerId(DeliveryManagerId deliveryManagerId) {
        validateNotNull(deliveryManagerId, DeliveryRouteDomainErrorCode.NULL_DELIVERY_MANAGER_ID_OBJECT);
    }

    private static void validateRouteInfo(RouteInfo routeInfo) {
        validateNotNull(routeInfo, DeliveryRouteDomainErrorCode.NULL_ROUTE_INFO_OBJECT);
    }
}
