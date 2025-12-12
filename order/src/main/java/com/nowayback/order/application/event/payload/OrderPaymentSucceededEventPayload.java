package com.nowayback.order.application.event.payload;

import com.nowayback.common.event.EventPayload;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OrderPaymentSucceededEventPayload implements EventPayload {
    private UUID orderId;

}
