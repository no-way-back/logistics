package com.nowayback.order.infrastructure.client.gemini;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowayback.order.application.client.GeminiClient;
import com.nowayback.order.application.client.request.GeminiDeadlinePrompt;
import com.nowayback.order.application.client.response.GeminiDeadlineResponse;
import com.nowayback.order.infrastructure.client.gemini.feign.GeminiFeignClient;
import com.nowayback.order.infrastructure.client.gemini.feign.request.GenerateContentFeignRequest;
import com.nowayback.order.infrastructure.client.gemini.feign.response.GenerateContentFeignResponse;
import com.nowayback.order.infrastructure.client.gemini.support.GeminiDeadlineJson;
import com.nowayback.order.infrastructure.client.gemini.support.GeminiPromptTemplates;
import com.nowayback.order.infrastructure.client.gemini.support.GeminiResponseExtractor;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClientImpl implements GeminiClient {

    private final GeminiFeignClient feign;

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.5-flash}")
    private String model;

    @Override
    public GeminiDeadlineResponse generateDeadline(GeminiDeadlinePrompt p) {
        String prompt = GeminiPromptTemplates.deadlinePrompt(p);
        GenerateContentFeignRequest request = GenerateContentFeignRequest.ofUserText(prompt);
        GenerateContentFeignResponse response = feign.generateContent(model, apiKey, request);

        String raw = GeminiResponseExtractor.firstText(response);
        if (raw == null || raw.isBlank()) {
            log.warn("[Gemini] empty text response");
            return new GeminiDeadlineResponse(null, null, false);
        }

        String cleaned = sanitizeToPureJson(raw);

        OffsetDateTime deadline = null;
        String reason = null;
        boolean confident = false;

        try {
            ObjectMapper mapper = new ObjectMapper();
            GeminiDeadlineJson dto = mapper.readValue(cleaned, GeminiDeadlineJson.class);
            reason = dto.reason();
            confident = Boolean.TRUE.equals(dto.confident());

            if (dto.deadline_iso() != null && !dto.deadline_iso().isBlank()) {
                try {
                    deadline = OffsetDateTime.parse(dto.deadline_iso());
                } catch (Exception e) {
                    LocalDateTime ldt = LocalDateTime.parse(dto.deadline_iso());
                    deadline = ldt.atZone(ZoneId.of("Asia/Seoul")).toOffsetDateTime();
                }
            }
        } catch (Exception e) {
            log.warn("[Gemini] JSON parse failed after sanitize, fallback parsing. cleaned={}", cleaned, e);
            deadline = tryParseIsoWithFallback(cleaned);
            reason = sliceAfter(cleaned, "\"reason\":");
            confident = cleaned.contains("\"confident\": true");
        }

        if (deadline == null) {
            log.warn("[Gemini] deadline parse failed. raw={}", raw);
        }

        return new GeminiDeadlineResponse(deadline, reason, confident);
    }

    /**
     * LLM이 ```json … ``` 또는 ``` … ```로 감싼 경우, 혹은 앞뒤 설명이 붙은 경우를 대비해
     * - 가장 첫 '{'부터 마지막 '}'까지를 슬라이싱
     * - 코드펜스(백틱)와 leading/trailing 노이즈 제거
     */
    private static String sanitizeToPureJson(String text) {
        String s = text.trim();

        // 코드펜스 제거 (```json, ``` 등)
        if (s.startsWith("```")) {
            // 맨 앞 코드펜스 한 번 제거
            int firstFenceEnd = s.indexOf('\n');
            if (firstFenceEnd > 0) {
                s = s.substring(firstFenceEnd + 1);
            }
            // 맨 뒤 코드펜스 제거
            int lastFence = s.lastIndexOf("```");
            if (lastFence >= 0) {
                s = s.substring(0, lastFence);
            }
            s = s.trim();
        }

        // 혹시 남아있는 역따옴표 제거 (안전장치)
        if (s.startsWith("`") || s.endsWith("`")) {
            s = s.replace("`", "");
            s = s.trim();
        }

        // JSON 본문만 추출: 첫 '{' ~ 마지막 '}' 구간 슬라이스
        int start = s.indexOf('{');
        int end = s.lastIndexOf('}');
        if (start >= 0 && end > start) {
            s = s.substring(start, end + 1).trim();
        }
        return s;
    }

    private static OffsetDateTime tryParseIsoWithFallback(String text) {
        if (text == null) return null;
        try {
            return OffsetDateTime.parse(sliceAfter(text, "\"deadline_iso\":"));
        } catch (Exception e1) {
            try {
                LocalDateTime ldt = LocalDateTime.parse(sliceAfter(text, "\"deadline_iso\":"));
                return ldt.atZone(ZoneId.of("Asia/Seoul")).toOffsetDateTime();
            } catch (Exception e2) {
                return null;
            }
        }
    }

    private static String sliceAfter(String text, String key) {
        if (text == null) return null;
        int i = text.indexOf(key);
        if (i < 0) return null;

        String tail = text.substring(i + key.length()).trim();
        // JSON 값일 경우 콜론과 공백/따옴표 제거
        if (tail.startsWith(":")) tail = tail.substring(1).trim();
        if (tail.startsWith("\"")) {
            int end = tail.indexOf('"', 1);
            return end > 0 ? tail.substring(1, end) : tail.replace("\"", "");
        }
        // 따옴표가 아니면 공백/쉼표/중괄호 기준으로 토큰 추정
        int j = 0;
        while (j < tail.length()) {
            char c = tail.charAt(j);
            if (c == ',' || c == '}' || c == '\n' || c == '\r') break;
            j++;
        }
        return tail.substring(0, j).trim();
    }
}
