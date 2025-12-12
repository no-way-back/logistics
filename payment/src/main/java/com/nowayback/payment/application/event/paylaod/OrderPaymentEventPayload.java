package com.nowayback.payment.application.event.paylaod;

import com.nowayback.common.event.EventPayload;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderPaymentEventPayload implements EventPayload {
    private UUID orderId;
    private UUID userId;
    private Long amount;
}

