package com.nowayback.order.infrastructure.client.gemini.support;

public record GeminiDeadlineJson(
    String deadline_iso,
    String reason,
    Boolean confident
) {}
