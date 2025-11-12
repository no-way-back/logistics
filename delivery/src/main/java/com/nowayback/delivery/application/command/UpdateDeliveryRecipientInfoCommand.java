package com.nowayback.delivery.application.command;

import com.nowayback.delivery.domain.delivery.vo.RecipientInfo;

public record UpdateDeliveryRecipientInfoCommand (
        RecipientInfo recipientInfo
) {

    public static UpdateDeliveryRecipientInfoCommand of(
            String deliveryAddress,
            String recipientName,
            String recipientSlackId
    ) {
        return new UpdateDeliveryRecipientInfoCommand(
                RecipientInfo.of(deliveryAddress, recipientName, recipientSlackId)
        );
    }
}
