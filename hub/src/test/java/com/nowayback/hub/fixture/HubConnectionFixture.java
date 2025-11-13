package com.nowayback.hub.fixture;


import com.nowayback.hub.domain.hubconnection.entity.HubConnection;
import com.nowayback.hub.domain.hubconnection.vo.ConnectedHubs;
import com.nowayback.hub.domain.hubconnection.vo.ConnectionInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.UUID;

/**
 * 테스트용 HubConnection Fixture
 */
public class HubConnectionFixture {

    /**
     * 기본 HubConnection 생성
     */
    public static HubConnection create(
            UUID originHubId,
            UUID destinationHubId,
            Integer distanceM,
            Integer estimatedDurationMin
    ) {
        try {
            HubConnection connection = createEmptyConnection();
            ConnectedHubs connectedHubs = createConnectedHubs(originHubId, destinationHubId);
            ConnectionInfo connectionInfo = createConnectionInfo(distanceM, estimatedDurationMin);

            setField(connection, "connectedHubs", connectedHubs);
            setField(connection, "connectionInfo", connectionInfo);

            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create HubConnection fixture", e);
        }
    }

    /**
     * 짧은 거리 연결 (50km, 30분)
     */
    public static HubConnection createShort(UUID originHubId, UUID destinationHubId) {
        return create(originHubId, destinationHubId, 50000, 30);
    }

    /**
     * 중간 거리 연결 (100km, 60분)
     */
    public static HubConnection createMedium(UUID originHubId, UUID destinationHubId) {
        return create(originHubId, destinationHubId, 100000, 60);
    }

    /**
     * 긴 거리 연결 (200km, 120분)
     */
    public static HubConnection createLong(UUID originHubId, UUID destinationHubId) {
        return create(originHubId, destinationHubId, 200000, 120);
    }

    // === Private Helper Methods ===

    private static HubConnection createEmptyConnection() throws Exception {
        Constructor<HubConnection> constructor = HubConnection.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private static ConnectedHubs createConnectedHubs(UUID originHubId, UUID destinationHubId) throws Exception {
        Constructor<ConnectedHubs> constructor = ConnectedHubs.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        ConnectedHubs connectedHubs = constructor.newInstance();

        setField(connectedHubs, "originHubId", originHubId);
        setField(connectedHubs, "destinationHubId", destinationHubId);

        return connectedHubs;
    }

    private static ConnectionInfo createConnectionInfo(Integer distanceM, Integer estimatedDurationMin) throws Exception {
        Constructor<ConnectionInfo> constructor = ConnectionInfo.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        ConnectionInfo connectionInfo = constructor.newInstance();

        setField(connectionInfo, "distanceM", distanceM);
        setField(connectionInfo, "estimatedDurationMin", estimatedDurationMin);

        return connectionInfo;
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}