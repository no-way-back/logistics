package com.nowayback.order.infrastructure.client.gemini.feign.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record GenerateContentFeignResponse(
    List<Candidate> candidates
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Candidate(
        Content content
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Content(
        List<Part> parts
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Part(
        String text
    ) {}
}
