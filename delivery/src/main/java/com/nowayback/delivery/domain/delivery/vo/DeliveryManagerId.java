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
public class DeliveryManagerId {

    private UUID id;

    private DeliveryManagerId(UUID id) {
        this.id = id;
    }

    public static DeliveryManagerId of(UUID id) {
        return new DeliveryManagerId(id);
    }
}
