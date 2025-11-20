package com.nowayback.delivery.domain.delivery.vo;

import com.nowayback.delivery.domain.delivery.exception.DeliveryDomainErrorCode;
import com.nowayback.delivery.domain.delivery.exception.DeliveryDomainException;
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
        if (id == null) {
            throw new DeliveryDomainException(DeliveryDomainErrorCode.NULL_DELIVERY_MANAGER_ID_VALUE);
        }
        return new DeliveryManagerId(id);
    }
}
