package com.nowayback.delivery.fixture;

import com.nowayback.delivery.domain.deliveryroute.entity.DeliveryRoute;
import com.nowayback.delivery.domain.deliveryroute.vo.*;

import java.lang.reflect.Field;
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
