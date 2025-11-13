package com.nowayback.delivery.domain.deliverymanager.vo;

import com.nowayback.delivery.domain.deliverymanager.exception.DeliveryManagerDomainErrorCode;
import com.nowayback.delivery.domain.deliverymanager.exception.DeliveryManagerDomainException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliverySequence {

    private int sequence;

    private DeliverySequence(int sequence) {
        this.sequence = sequence;
    }

    public static DeliverySequence of(int sequence) {
        if (sequence < 0) {
            throw new DeliveryManagerDomainException(DeliveryManagerDomainErrorCode.NEGATIVE_DELIVERY_SEQUENCE_VALUE);
        }
        return new DeliverySequence(sequence);
    }
}
