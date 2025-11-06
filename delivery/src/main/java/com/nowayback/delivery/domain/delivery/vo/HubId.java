package com.nowayback.delivery.domain.delivery.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubId {

    private UUID id;

    private HubId(UUID id) {
        this.id = id;
    }

    public static HubId of(UUID id) {
        return new HubId(id);
    }
}
