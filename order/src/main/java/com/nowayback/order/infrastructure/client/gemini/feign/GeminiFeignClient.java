package com.nowayback.order.infrastructure.client.gemini.feign;

import com.nowayback.order.infrastructure.client.gemini.feign.request.GenerateContentFeignRequest;
import com.nowayback.order.infrastructure.client.gemini.feign.response.GenerateContentFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "gemini", url = "${gemini.base-url}")
public interface GeminiFeignClient {
    @PostMapping("/v1beta/models/{model}:generateContent")
    GenerateContentFeignResponse generateContent(
        @PathVariable("model") String model,
        @RequestParam("key") String apiKey,
        @RequestBody GenerateContentFeignRequest request
    );
}
