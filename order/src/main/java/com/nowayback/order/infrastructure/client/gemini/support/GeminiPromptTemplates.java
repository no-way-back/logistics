package com.nowayback.order.infrastructure.client.gemini.support;

import com.nowayback.order.application.client.request.GeminiDeadlinePrompt;
import java.time.format.DateTimeFormatter;

public final class GeminiPromptTemplates {
    private GeminiPromptTemplates() {}

    public static String deadlinePrompt(GeminiDeadlinePrompt p) {
        String createdAt = p.orderCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String items = p.itemsSummary() == null ? "" : p.itemsSummary();
        String note = p.requestNote() == null ? "" : p.requestNote();

        return """
        당신은 물류 스케줄러 보조원입니다.
        입력 정보를 모두 고려하여 '최종 발송 시한'을 산출하세요.

        규칙:
        - Asia/Seoul 기준으로 계산하세요.
        - 반드시 아래 JSON만 출력하세요. 다른 텍스트는 절대 포함하지 마세요.
        - **백틱(`)과 코드블록(``` 등) 사용 금지**, 주석 금지, 앞뒤 설명 금지.

        {
          "deadline_iso": "YYYY-MM-DDThh:mm:ss+09:00",
          "reason": "짧은 근거",
          "confident": true
        }

        입력:
        - orderId: %s
        - createdAt: %s
        - items: %s
        - requestNote: %s
        - from: %s (%s)
        - to: %s (%s)
        - handlerWorkingHours: %02d~%02d
        - locale: %s
        """.formatted(
            p.orderId(),
            createdAt,
            items,
            note,
            safe(p.from().name()), safe(p.from().address()),
            safe(p.to().name()), safe(p.to().address()),
            p.handlerWorkingHours().startHour(), p.handlerWorkingHours().endHour(),
            p.locale()
        );
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
