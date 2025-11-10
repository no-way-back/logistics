package com.nowayback.hub.domain.hubconnection.entity;

import com.nowayback.common.audit.BaseEntity;
import com.nowayback.hub.domain.hubconnection.vo.ConnectedHubs;
import com.nowayback.hub.domain.hubconnection.vo.ConnectionInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_hub_connections")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubConnection extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "connection_id", nullable = false)
    private UUID connectionId;

    @Embedded
    private ConnectedHubs connectedHubs;

    @Embedded
    private ConnectionInfo connectionInfo;
}