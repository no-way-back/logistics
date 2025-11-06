package com.nowayback.delivery.domain.delivery.entity;

import audit.BaseEntity;
import com.nowayback.delivery.domain.delivery.exception.DeliveryErrorCode;
import com.nowayback.delivery.domain.delivery.exception.InvalidHubRouteException;
import com.nowayback.delivery.domain.delivery.vo.*;
import com.nowayback.delivery.domain.exception.InvalidObjectException;
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

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "order_id", updatable = false, nullable = false))
    private OrderId orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "source_hub_id", updatable = false, nullable = false))
    private HubId sourceHubId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "destination_hub_id", updatable = false, nullable = false))
    private HubId destinationHubId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "delivery_address")),
            @AttributeOverride(name = "name", column = @Column(name = "recipient_name")),
            @AttributeOverride(name = "slackId", column = @Column(name = "recipient_slack_id"))
    })
    private RecipientInfo recipientInfo;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "company_delivery_manager_id", nullable = false))
    private DeliveryManagerId companyDeliveryManagerId;

    private Delivery(OrderId orderId, DeliveryStatus status, HubId sourceHubId, HubId destinationHubId, RecipientInfo recipientInfo, DeliveryManagerId companyDeliveryManagerId) {
        this.orderId = orderId;
        this.status = status;
        this.sourceHubId = sourceHubId;
        this.destinationHubId = destinationHubId;
        this.recipientInfo = recipientInfo;
        this.companyDeliveryManagerId = companyDeliveryManagerId;
    }

    public static Delivery create(OrderId orderId, HubId sourceHubId, HubId destinationHubId, RecipientInfo recipientInfo, DeliveryManagerId companyDeliveryManagerId) {
        validateOrderId(orderId);
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

    private static void validateHubRoute(HubId sourceHubId, HubId destinationHubId) {
        if (sourceHubId.equals(destinationHubId)) {
            throw new InvalidHubRouteException(DeliveryErrorCode.INVALID_HUB_ROUTE);
        }
    }

    private static void validateOrderId(OrderId orderId) {
        if (orderId == null) { throw new InvalidObjectException(DeliveryErrorCode.NULL_ORDER_ID_OBJECT); }
    }
}
