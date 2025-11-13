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
public class RouteInfo {

    private Integer expectedDistanceMeters;
    private Integer expectedDurationMinutes;
    private Integer actualDistanceMeters;
    private Integer actualDurationMinutes;

    public RouteInfo(Integer expectedDistanceMeters, Integer expectedDurationMinutes, Integer actualDistanceMeters, Integer actualDurationMinutes) {
        this.expectedDistanceMeters = expectedDistanceMeters;
        this.expectedDurationMinutes = expectedDurationMinutes;
        this.actualDistanceMeters = actualDistanceMeters;
        this.actualDurationMinutes = actualDurationMinutes;
    }

    public static RouteInfo of(Integer expectedDistanceMeters, Integer expectedDurationMinutes, Integer actualDistanceMeters, Integer actualDurationMinutes) {
        validateDistanceMeters(expectedDistanceMeters);
        validateDurationMinutes(expectedDurationMinutes);
        validateDistanceMeters(actualDistanceMeters);
        validateDurationMinutes(actualDurationMinutes);

        return new RouteInfo(expectedDistanceMeters, expectedDurationMinutes, actualDistanceMeters, actualDurationMinutes);
    }

    private static void validateDistanceMeters(Integer distanceMeters) {
        if (distanceMeters != null && distanceMeters < 0) {
            throw new DeliveryRouteDomainException(DeliveryRouteDomainErrorCode.INVALID_DISTANCE_METERS);
        }
    }

    private static void validateDurationMinutes(Integer durationMinutes) {
        if (durationMinutes != null && durationMinutes < 0) {
            throw new DeliveryRouteDomainException(DeliveryRouteDomainErrorCode.INVALID_DURATION_MINUTES);
        }
    }
}
