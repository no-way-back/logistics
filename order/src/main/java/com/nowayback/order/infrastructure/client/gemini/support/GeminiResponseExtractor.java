package com.nowayback.order.infrastructure.client.gemini.support;

import com.nowayback.order.infrastructure.client.gemini.feign.response.GenerateContentFeignResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class GeminiResponseExtractor {

    public static String firstText(GenerateContentFeignResponse res) {
        if (res == null) {
            log.warn("[gemini] response is null");
            return "";
        }
        var candidates = res.candidates();
        if (candidates == null || candidates.isEmpty()) {
            log.warn("[gemini] candidates is empty");
            return "";
        }
        var c0 = candidates.get(0);
        if (c0 == null || c0.content() == null) {
            log.warn("[gemini] first candidate or its content is null");
            return "";
        }
        var parts = c0.content().parts();
        if (parts == null || parts.isEmpty()) {
            log.warn("[gemini] parts is empty");
            return "";
        }
        var p0 = parts.get(0);
        var text = p0 != null ? p0.text() : null;
        return text != null ? text : "";
    }
}
