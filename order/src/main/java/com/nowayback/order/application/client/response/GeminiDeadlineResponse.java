package com.nowayback.order.application.client.response;

import java.time.OffsetDateTime;

public record GeminiDeadlineResponse(
    OffsetDateTime finalDispatchDeadline,
    String rationale,
    boolean confident
) {

    public static GeminiDeadlineResponse of(
        OffsetDateTime deadline, String rationale, boolean confident
    ) {
        return new GeminiDeadlineResponse(deadline, rationale, confident);
    }
}
