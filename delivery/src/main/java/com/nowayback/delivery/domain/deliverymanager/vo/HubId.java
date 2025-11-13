package com.nowayback.delivery.domain.deliverymanager.vo;

import com.nowayback.delivery.domain.deliverymanager.exception.DeliveryManagerDomainErrorCode;
import com.nowayback.delivery.domain.deliverymanager.exception.DeliveryManagerDomainException;
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
        if (id == null) {
            throw new DeliveryManagerDomainException(DeliveryManagerDomainErrorCode.NULL_HUB_ID_VALUE);
        }
        return new HubId(id);
    }
}
