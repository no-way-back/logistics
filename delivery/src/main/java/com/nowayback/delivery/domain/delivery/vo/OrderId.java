package com.nowayback.delivery.domain.delivery.vo;

import com.nowayback.delivery.domain.delivery.exception.DeliveryErrorCode;
import com.nowayback.delivery.domain.exception.InvalidValueException;
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
public class OrderId {

    private UUID id;

    private OrderId(UUID id) {
        this.id = id;
    }

    public static OrderId of(UUID id) {
        if (id == null) {
            throw new InvalidValueException(DeliveryErrorCode.NULL_ORDER_ID_VALUE);
        }
        return new OrderId(id);
    }
}
