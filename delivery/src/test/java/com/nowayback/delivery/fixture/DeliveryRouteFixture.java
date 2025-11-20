package com.nowayback.delivery.fixture;

import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.delivery.application.deliveryroute.command.CreateDeliveryRoutesCommand;
import com.nowayback.delivery.application.deliveryroute.command.UpdateDeliveryRouteInfoCommand;
import com.nowayback.delivery.application.deliveryroute.command.UpdateDeliveryRouteStatusCommand;
import com.nowayback.delivery.application.deliveryroute.dto.DeliveryRouteResult;
import com.nowayback.delivery.domain.deliveryroute.entity.DeliveryRoute;
import com.nowayback.delivery.domain.deliveryroute.vo.*;
import com.nowayback.delivery.presentation.deliveryroute.dto.request.UpdateDeliveryRouteInfoRequest;
import com.nowayback.delivery.presentation.deliveryroute.dto.request.UpdateDeliveryRouteStatusRequest;
import org.springframework.data.domain.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class DeliveryRouteFixture {

    public static final UUID DELIVERY_ROUTE_UUID = UUID.randomUUID();

    public static final UUID DELIVERY_UUID = UUID.randomUUID();
    public static final int SEQUENCE = 1;
    public static final UUID HUB_UUID = UUID.randomUUID();
    public static final UUID SOURCE_HUB_UUID = UUID.randomUUID();
    public static final UUID DESTINATION_HUB_UUID = UUID.randomUUID();
    public static final UUID DELIVERY_MANAGER_UUID = UUID.randomUUID();

    public static final Integer EXPECTED_DISTANCE_METERS = 1000;
    public static final Integer EXPECTED_DURATION_MINUTES = 15;
    public static final Integer ACTUAL_DISTANCE_METERS = 1200;
    public static final Integer ACTUAL_DURATION_MINUTES = 20;

    public static final DeliveryId DELIVERY_ID = DeliveryId.of(DELIVERY_UUID);
    public static final RouteSequence ROUTE_SEQUENCE = RouteSequence.of(SEQUENCE);
    public static final HubId HUB_ID = HubId.of(HUB_UUID);
    public static final HubId SOURCE_HUB_ID = HubId.of(SOURCE_HUB_UUID);
    public static final HubId DESTINATION_HUB_ID = HubId.of(DESTINATION_HUB_UUID);
    public static final HubRoute HUB_ROUTE = HubRoute.of(SOURCE_HUB_ID, DESTINATION_HUB_ID);
    public static final DeliveryManagerId DELIVERY_MANAGER_ID = DeliveryManagerId.of(DELIVERY_MANAGER_UUID);
    public static final RouteInfo ROUTE_INFO = RouteInfo.of(
            EXPECTED_DISTANCE_METERS,
            EXPECTED_DURATION_MINUTES,
            null,
            null
    );
    public static final RouteInfo MODIFIED_ROUTE_INFO = RouteInfo.of(
            EXPECTED_DISTANCE_METERS,
            EXPECTED_DURATION_MINUTES,
            ACTUAL_DISTANCE_METERS,
            ACTUAL_DURATION_MINUTES
    );

    public static final UUID USER_UUID = UUID.randomUUID();
    public static final UserRole USER_ROLE = UserRole.MASTER;

    public static final int PAGE = 0;
    public static final int SIZE = 10;

    /* delivery route entity */
    public static DeliveryRoute createDeliveryRoute() {
        return DeliveryRoute.create(
                DELIVERY_ID,
                ROUTE_SEQUENCE,
                HUB_ROUTE,
                DELIVERY_MANAGER_ID,
                ROUTE_INFO
        );
    }

    public static DeliveryRoute createDeliveryRoute(DeliveryRouteStatus routeStatus) {
        DeliveryRoute route = createDeliveryRoute();
        setPrivateField(route, "status", routeStatus);
        return route;
    }

    public static DeliveryRoute createDeliveryRoute(DeliveryId deliveryId, RouteSequence routeSequence) {
        return DeliveryRoute.create(
                deliveryId,
                routeSequence,
                HUB_ROUTE,
                DELIVERY_MANAGER_ID,
                ROUTE_INFO
        );
    }

    private static final Sort SORT = Sort.by("sequence").ascending();
    public static final Pageable PAGEABLE = PageRequest.of(PAGE, SIZE, SORT);
    public static final List<DeliveryRoute> DELIVERY_ROUTES = List.of(createDeliveryRoute());
    public static final Page<DeliveryRoute> DELIVERY_ROUTES_PAGE = new PageImpl<>(DELIVERY_ROUTES, PAGEABLE, DELIVERY_ROUTES.size());

    /* delivery route command */
    public static final CreateDeliveryRoutesCommand.DeliveryRouteSegment DELIVERY_ROUTE_SEGMENT = CreateDeliveryRoutesCommand.DeliveryRouteSegment.of(
            SEQUENCE,
            SOURCE_HUB_UUID,
            DESTINATION_HUB_UUID,
            EXPECTED_DISTANCE_METERS,
            EXPECTED_DURATION_MINUTES
    );

    public static final CreateDeliveryRoutesCommand CREATE_DELIVERY_ROUTES_COMMAND = CreateDeliveryRoutesCommand.of(
            DELIVERY_UUID,
            List.of(DELIVERY_ROUTE_SEGMENT)
    );

    public static final CreateDeliveryRoutesCommand CREATE_DELIVERY_ROUTES_COMMAND_WITH_DUPLICATE_SEQUENCE = CreateDeliveryRoutesCommand.of(
            DELIVERY_UUID,
            List.of(DELIVERY_ROUTE_SEGMENT, DELIVERY_ROUTE_SEGMENT)
    );

    public static final UpdateDeliveryRouteStatusCommand UPDATE_DELIVERY_ROUTE_STATUS_COMMAND = UpdateDeliveryRouteStatusCommand.of(
            DeliveryRouteStatus.TRANSIT_BETWEEN_HUBS
    );

    public static final UpdateDeliveryRouteInfoCommand UPDATE_DELIVERY_ROUTE_INFO_COMMAND = UpdateDeliveryRouteInfoCommand.of(
            ACTUAL_DISTANCE_METERS,
            ACTUAL_DURATION_MINUTES
    );

    /* delivery route result */
    public static final DeliveryRouteResult DELIVERY_ROUTE_RESULT = DeliveryRouteResult.from(createDeliveryRoute());
    public static final Page<DeliveryRouteResult> DELIVERY_ROUTE_RESULT_PAGE = DELIVERY_ROUTES_PAGE.map(DeliveryRouteResult::from);

    public static final DeliveryRouteResult MODIFIED_STATUS_DELIVERY_ROUTE_RESULT = DeliveryRouteResult.from(createDeliveryRoute(DeliveryRouteStatus.TRANSIT_BETWEEN_HUBS));
    public static DeliveryRouteResult getModifiedInfoDeliveryRouteResult() {
        DeliveryRoute route = createDeliveryRoute(DeliveryRouteStatus.AT_DESTINATION_HUB);
        route.updateRouteInfo(MODIFIED_ROUTE_INFO);
        return DeliveryRouteResult.from(route);
    }

    /* delivery route request */
    public static final UpdateDeliveryRouteStatusRequest UPDATE_DELIVERY_ROUTE_STATUS_REQUEST = new UpdateDeliveryRouteStatusRequest(
            DeliveryRouteStatus.TRANSIT_BETWEEN_HUBS
    );
    public static final UpdateDeliveryRouteStatusRequest INVALID_UPDATE_DELIVERY_ROUTE_STATUS_REQUEST = new UpdateDeliveryRouteStatusRequest(
            null
    );
    public static final UpdateDeliveryRouteInfoRequest UPDATE_DELIVERY_ROUTE_INFO_REQUEST = new UpdateDeliveryRouteInfoRequest(
            ACTUAL_DISTANCE_METERS,
            ACTUAL_DURATION_MINUTES
    );
    public static final UpdateDeliveryRouteInfoRequest INVALID_UPDATE_DELIVERY_ROUTE_INFO_REQUEST = new UpdateDeliveryRouteInfoRequest(
            -100,
            100
    );

    private static void setPrivateField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
