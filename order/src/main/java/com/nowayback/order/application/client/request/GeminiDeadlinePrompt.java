// order/application/client/request/GeminiDeadlinePrompt.java
package com.nowayback.order.application.client.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record GeminiDeadlinePrompt(
    UUID orderId,
    String itemsSummary,
    String requestNote,
    Location from,
    Location to,
    WorkingHours handlerWorkingHours,
    LocalDateTime orderCreatedAt,
    String locale
) {
    public record Location(String name, String address) {}
    public record WorkingHours(int startHour, int endHour) {}
}
