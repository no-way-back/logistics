package com.nowayback.order.application.event.payload;

import com.nowayback.common.event.EventPayload;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class OrderPaymentFailedEventPayload implements EventPayload {
    private UUID orderId;
}
