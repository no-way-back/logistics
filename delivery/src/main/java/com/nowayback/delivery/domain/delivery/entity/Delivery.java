package com.nowayback.delivery.domain.delivery.entity;

import audit.BaseEntity;
import com.nowayback.delivery.domain.delivery.exception.DeliveryErrorCode;
import com.nowayback.delivery.domain.delivery.exception.InvalidHubRouteException;
import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;
import com.nowayback.delivery.domain.delivery.vo.RecipientInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_deliveries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "order_id", updatable = false, nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status;

    @Column(name = "source_hub_id", updatable = false, nullable = false)
    private UUID sourceHubId;

    @Column(name = "destination_hub_id", updatable = false, nullable = false)
    private UUID destinationHubId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "delivery_address")),
            @AttributeOverride(name = "name", column = @Column(name = "recipient_name")),
            @AttributeOverride(name = "slackId", column = @Column(name = "recipient_slack_id"))
    })
    private RecipientInfo recipientInfo;

    @Column(name = "company_delivery_manager_id", nullable = false)
    private UUID companyDeliveryManagerId;

    private Delivery(UUID orderId, DeliveryStatus status, UUID sourceHubId, UUID destinationHubId, RecipientInfo recipientInfo, UUID companyDeliveryManagerId) {
        this.orderId = orderId;
        this.status = status;
        this.sourceHubId = sourceHubId;
        this.destinationHubId = destinationHubId;
        this.recipientInfo = recipientInfo;
        this.companyDeliveryManagerId = companyDeliveryManagerId;
    }

    public static Delivery create(UUID orderId, UUID sourceHubId, UUID destinationHubId, RecipientInfo recipientInfo, UUID companyDeliveryManagerId) {
        validateHubRoute(sourceHubId, destinationHubId);

        return new Delivery(
                orderId,
                DeliveryStatus.WAITING_AT_HUB,
                sourceHubId,
                destinationHubId,
                recipientInfo,
                companyDeliveryManagerId
        );
    }

    private static void validateHubRoute(UUID sourceHubId, UUID destinationHubId) {
        if (sourceHubId.equals(destinationHubId)) {
            throw new InvalidHubRouteException(DeliveryErrorCode.INVALID_HUB_ROUTE);
        }
    }
}
