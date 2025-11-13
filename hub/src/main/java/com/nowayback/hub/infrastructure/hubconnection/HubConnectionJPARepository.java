package com.nowayback.hub.infrastructure.hubconnection;

import com.nowayback.hub.domain.hubconnection.entity.HubConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface HubConnectionJPARepository extends JpaRepository<HubConnection, UUID> {

    /**
     * 특정 허브에서 출발하는 모든 연결 조회
     */
    @Query("SELECT hc FROM HubConnection hc " +
            "WHERE hc.connectedHubs.originHubId = :hubId")
    List<HubConnection> findByOriginHubId(UUID hubId);

    /**
     * 특정 허브로 도착하는 모든 연결 조회
     */
    @Query("SELECT hc FROM HubConnection hc " +
            "WHERE hc.connectedHubs.destinationHubId = :hubId")
    List<HubConnection> findByDestinationHubId(UUID hubId);
}
