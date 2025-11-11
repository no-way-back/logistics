package com.nowayback.delivery.application.command;

import com.nowayback.delivery.presentation.dto.request.UpdateDeliveryRecipientInfoRequest;

public record UpdateDeliveryRecipientInfoCommand (
        String deliveryAddress,
        String recipientName,
        String recipientSlackId
) {

    public static UpdateDeliveryRecipientInfoCommand from(UpdateDeliveryRecipientInfoRequest request) {
        return new UpdateDeliveryRecipientInfoCommand(
                request.deliveryAddress(),
                request.recipientName(),
                request.recipientSlackId()
        );
    }
}
