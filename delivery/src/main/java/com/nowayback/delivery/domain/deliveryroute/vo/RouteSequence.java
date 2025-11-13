package com.nowayback.delivery.domain.deliveryroute.vo;

import com.nowayback.delivery.domain.exception.DeliveryRouteDomainErrorCode;
import com.nowayback.delivery.domain.exception.DeliveryRouteDomainException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteSequence {

    private int sequence;

    private RouteSequence(int sequence) {
        this.sequence = sequence;
    }

    public static RouteSequence of(int sequence) {
        validateSequence(sequence);
        return new RouteSequence(sequence);
    }

    private static void validateSequence(int sequence) {
        if (sequence < 0) {
            throw new DeliveryRouteDomainException(DeliveryRouteDomainErrorCode.INVALID_ROUTE_SEQUENCE);
        }
    }
}
