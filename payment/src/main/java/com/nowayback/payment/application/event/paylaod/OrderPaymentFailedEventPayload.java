package com.nowayback.payment.application.event.paylaod;

import com.nowayback.common.event.EventPayload;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentFailedEventPayload implements EventPayload {
    private UUID orderId;

    public static OrderPaymentFailedEventPayload create(UUID orderId) {
        return new OrderPaymentFailedEventPayload(orderId);
    }
}
