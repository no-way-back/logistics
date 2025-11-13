package com.nowayback.order.application.client;

import com.nowayback.order.application.client.request.GeminiDeadlinePrompt;
import com.nowayback.order.application.client.response.GeminiDeadlineResponse;

public interface GeminiClient {
    GeminiDeadlineResponse generateDeadline(GeminiDeadlinePrompt prompt);
}
