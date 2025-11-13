package com.nowayback.order.infrastructure.client.gemini.feign.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GenerateContentFeignRequest(
    List<Content> contents
) {
    public static GenerateContentFeignRequest ofUserText(String text) {
        return new GenerateContentFeignRequest(
            List.of(new Content("user", List.of(Part.text(text))))
        );
    }

    public record Content(
        String role,
        List<Part> parts
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Part(
        String text
    ) {
        public static Part text(String t) { return new Part(t); }
    }
}
