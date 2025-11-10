package com.nowayback.hub.domain.hubmanager.entity;

import com.nowayback.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_hub_managers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubManager extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_manager_id", nullable = false)
    private UUID hubManagerId;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;
}
