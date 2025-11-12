package com.nowayback.delivery.domain.deliverymanager.entity;

import com.nowayback.common.audit.BaseEntity;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliverySequence;
import com.nowayback.delivery.domain.deliverymanager.vo.HubId;
import com.nowayback.delivery.domain.exception.DeliveryManagerDomainErrorCode;
import com.nowayback.delivery.domain.exception.DeliveryManagerDomainException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_delivery_managers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryManager extends BaseEntity {

    @Id
    @Column(name = "delivery_manager_id", updatable = false, nullable = false)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "hub_id", nullable = false))
    private HubId hubId;

    @Enumerated(EnumType.STRING)
    @Column(name = "manager_type", nullable = false)
    private DeliveryManagerType type;

    @Column(name = "slack_id", nullable = false)
    private String slackId;

    @Embedded
    @AttributeOverride(name = "sequence", column = @Column(name = "delivery_sequence", nullable = false))
    private DeliverySequence deliverySequence;

    public DeliveryManager(UUID id, HubId hubId, DeliveryManagerType type, String slackId, DeliverySequence deliverySequence) {
        this.id = id;
        this.hubId = hubId;
        this.type = type;
        this.slackId = slackId;
        this.deliverySequence = deliverySequence;
    }

    public static DeliveryManager create(UUID userId, HubId hubId, DeliveryManagerType type, String slackId, DeliverySequence deliverySequence) {
        validateHubId(hubId);
        validateDeliveryManagerType(type);
        validateSlackId(slackId);
        validateDeliverySequence(deliverySequence);

        return new DeliveryManager(
                userId,
                hubId,
                type,
                slackId,
                deliverySequence
        );
    }

    public void delete(UUID deletedBy) {
        softDelete(deletedBy);
    }

    private static void validateUserId(UUID userId) {
        if (userId == null) {
            throw new DeliveryManagerDomainException(DeliveryManagerDomainErrorCode.NULL_USER_ID_VALUE);
        }
    }

    private static void validateHubId(HubId hubId) {
        if (hubId == null) {
            throw new DeliveryManagerDomainException(DeliveryManagerDomainErrorCode.NULL_HUB_ID_OBJECT);
        }
    }

    private static void validateDeliveryManagerType(DeliveryManagerType type) {
        if (type == null) {
            throw new DeliveryManagerDomainException(DeliveryManagerDomainErrorCode.NULL_DELIVERY_MANAGER_TYPE_OBJECT);
        }
    }

    private static void validateSlackId(String slackId) {
        if (slackId == null || slackId.isBlank()) {
            throw new DeliveryManagerDomainException(DeliveryManagerDomainErrorCode.NULL_SLACK_ID_VALUE);
        }
    }

    private static void validateDeliverySequence(DeliverySequence deliverySequence) {
        if (deliverySequence == null) {
            throw new DeliveryManagerDomainException(DeliveryManagerDomainErrorCode.NULL_DELIVERY_SEQUENCE_OBJECT);
        }
    }
}
