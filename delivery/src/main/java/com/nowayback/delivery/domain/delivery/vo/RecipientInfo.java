package com.nowayback.delivery.domain.delivery.vo;

import com.nowayback.delivery.domain.exception.DeliveryDomainErrorCode;
import com.nowayback.delivery.domain.exception.DeliveryDomainException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipientInfo {

    private String address;
    private String name;
    private String slackId;

    private RecipientInfo(String address, String name, String slackId) {
        this.address = address;
        this.name = name;
        this.slackId = slackId;
    }

    public static RecipientInfo of(String address, String name, String slackId) {
        validateAddress(address);
        validateName(name);
        validateSlackId(slackId);

        return new RecipientInfo(address, name, slackId);
    }

    private static void validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new DeliveryDomainException(DeliveryDomainErrorCode.INVALID_RECIPIENT_ADDRESS);
        }
    }

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new DeliveryDomainException(DeliveryDomainErrorCode.INVALID_RECIPIENT_NAME);
        }
    }

    private static void validateSlackId(String slackId) {
        if (slackId == null || slackId.trim().isEmpty()) {
            throw new DeliveryDomainException(DeliveryDomainErrorCode.INVALID_RECIPIENT_SLACK_ID);
        }
    }
}
