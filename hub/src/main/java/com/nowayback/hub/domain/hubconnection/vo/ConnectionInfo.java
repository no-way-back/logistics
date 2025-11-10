package com.nowayback.hub.domain.hubconnection.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConnectionInfo {

    @Column(name = "distance_m", nullable = false)
    private Integer distanceM;

    @Column(name = "estimated_duration_min", nullable = false)
    private Integer estimatedDurationMin;
}