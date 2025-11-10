package com.nowayback.delivery.application.command;

public record UpdateDeliveryRecipientInfoCommand (
        String deliveryAddress,
        String recipientName,
        String recipientSlackId
) {
}
