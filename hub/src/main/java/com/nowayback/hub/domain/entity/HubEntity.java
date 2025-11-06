package com.nowayback.hub.domain.entity;

import audit.BaseEntity;
import com.nowayback.hub.application.command.CreateHubCommand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "p_hubs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    public static HubEntity create(CreateHubCommand command) {
        HubEntity hubEntity = new HubEntity();
        hubEntity.name = command.name();
        hubEntity.address = command.address();
        hubEntity.latitude = command.latitude();
        hubEntity.longitude = command.longitude();
        return hubEntity;
    }

}
