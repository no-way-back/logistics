package com.nowayback.delivery.domain.deliveryroute.vo;

import com.nowayback.delivery.domain.exception.DeliveryRouteDomainErrorCode;
import com.nowayback.delivery.domain.exception.DeliveryRouteDomainException;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubRoute {

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "source_hub_id", nullable = false, updatable = false))
    private HubId sourceHubId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "destination_hub_id", nullable = false, updatable = false))
    private HubId destinationHubId;

    private HubRoute(HubId sourceHubId, HubId destinationHubId) {
        this.sourceHubId = sourceHubId;
        this.destinationHubId = destinationHubId;
    }

    public static HubRoute of(HubId sourceHubId, HubId destinationHubId) {
        validateHubIds(sourceHubId, destinationHubId);
        validateHubRoute(sourceHubId, destinationHubId);

        return new HubRoute(sourceHubId, destinationHubId);
    }

    private static void validateHubIds(HubId sourceHubId, HubId destinationHubId) {
        if (sourceHubId == null) {
            throw new DeliveryRouteDomainException(DeliveryRouteDomainErrorCode.NULL_SOURCE_HUB_ID);
        }
        if (destinationHubId == null) {
            throw new DeliveryRouteDomainException(DeliveryRouteDomainErrorCode.NULL_DESTINATION_HUB_ID);
        }
    }

    private static void validateHubRoute(HubId sourceHubId, HubId destinationHubId) {
        if (sourceHubId.equals(destinationHubId)) {
            throw new DeliveryRouteDomainException(DeliveryRouteDomainErrorCode.INVALID_HUB_ROUTE_SAME_HUB);
        }
    }
}
