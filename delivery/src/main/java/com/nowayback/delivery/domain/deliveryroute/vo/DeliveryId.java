package com.nowayback.delivery.domain.deliveryroute.vo;

import com.nowayback.delivery.domain.deliveryroute.exception.DeliveryRouteDomainErrorCode;
import com.nowayback.delivery.domain.deliveryroute.exception.DeliveryRouteDomainException;
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
public class DeliveryId {

    private UUID id;

    private DeliveryId(UUID id) {
        this.id = id;
    }

    public static DeliveryId of(UUID id) {
        if (id == null) {
            throw new DeliveryRouteDomainException(DeliveryRouteDomainErrorCode.NULL_DELIVERY_ID_VALUE);
        }
        return new DeliveryId(id);
    }
}
