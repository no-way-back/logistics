package com.nowayback.delivery.domain.delivery.entity;

import com.nowayback.common.audit.BaseEntity;
import com.nowayback.delivery.domain.delivery.exception.DeliveryDomainErrorCode;
import com.nowayback.delivery.domain.delivery.vo.*;
import com.nowayback.delivery.domain.delivery.exception.DeliveryDomainException;
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

    public static Delivery create(OrderId orderId, HubId sourceHubId, HubId destinationHubId, RecipientInfo recipientInfo, DeliveryManagerId companyDeliveryManagerId) {
        validateOrderId(orderId);
        validateSourceHubId(sourceHubId);
        validateDestinationHubId(destinationHubId);
        validateHubRoute(sourceHubId, destinationHubId);
        validateRecipientInfo(recipientInfo);
        validateCompanyDeliveryManagerId(companyDeliveryManagerId);

        return new Delivery(
                orderId,
                DeliveryStatus.WAITING_AT_HUB,
                sourceHubId,
                destinationHubId,
                recipientInfo,
                companyDeliveryManagerId
        );
    }

    public void updateRecipientInfo(RecipientInfo newRecipientInfo) {
        validateNotNull(newRecipientInfo, DeliveryDomainErrorCode.NULL_RECIPIENT_INFO_OBJECT);

        if (!status.canUpdateRecipientInfo()) {
            throw new DeliveryDomainException(DeliveryDomainErrorCode.INVALID_DELIVERY_STATUS_FOR_UPDATE);
        }

        this.recipientInfo = newRecipientInfo;
    }

    public void updateStatus(DeliveryStatus newStatus) {
        if (!status.canTransitionTo(newStatus)) {
            throw new DeliveryDomainException(DeliveryDomainErrorCode.INVALID_DELIVERY_STATUS_TRANSITION);
        }
        this.status = newStatus;
    }

    public void delete(UUID deletedBy) {
        if (!status.canBeDeleted()) {
            throw new DeliveryDomainException(DeliveryDomainErrorCode.INVALID_DELIVERY_STATUS_FOR_DELETE);
        }
        softDelete(deletedBy);
    }

    private static void validateHubRoute(HubId sourceHubId, HubId destinationHubId) {
        if (sourceHubId.equals(destinationHubId)) {
            throw new DeliveryDomainException(DeliveryDomainErrorCode.INVALID_HUB_ROUTE);
        }
    }

    private static void validateNotNull(Object object, DeliveryDomainErrorCode errorCode) {
        if (object == null) throw new DeliveryDomainException(errorCode);
    }

    private static void validateOrderId(OrderId orderId) {
        validateNotNull(orderId, DeliveryDomainErrorCode.NULL_ORDER_ID_OBJECT);
    }

    private static void validateSourceHubId(HubId sourceHubId) {
        validateNotNull(sourceHubId, DeliveryDomainErrorCode.NULL_SOURCE_HUB_ID_OBJECT);
    }

    private static void validateDestinationHubId(HubId destinationHubId) {
        validateNotNull(destinationHubId, DeliveryDomainErrorCode.NULL_DESTINATION_HUB_ID_OBJECT);
    }

    private static void validateRecipientInfo(RecipientInfo recipientInfo) {
        validateNotNull(recipientInfo, DeliveryDomainErrorCode.NULL_RECIPIENT_INFO_OBJECT);
    }

    private static void validateCompanyDeliveryManagerId(DeliveryManagerId companyDeliveryManagerId) {
        validateNotNull(companyDeliveryManagerId, DeliveryDomainErrorCode.NULL_DELIVERY_MANAGER_ID_OBJECT);
    }

    private Delivery(OrderId orderId, DeliveryStatus status, HubId sourceHubId, HubId destinationHubId, RecipientInfo recipientInfo, DeliveryManagerId companyDeliveryManagerId) {
        this.orderId = orderId;
        this.status = status;
        this.sourceHubId = sourceHubId;
        this.destinationHubId = destinationHubId;
        this.recipientInfo = recipientInfo;
        this.companyDeliveryManagerId = companyDeliveryManagerId;
    }
}
