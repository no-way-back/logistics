package com.nowayback.order.infrastructure.client;

import com.nowayback.order.application.client.ProductClient;
import com.nowayback.order.application.client.request.DecreaseStockRequest;
import com.nowayback.order.application.client.response.DecreaseStockResponse;
import com.nowayback.order.infrastructure.client.feign.ProductFeignClient;
import com.nowayback.order.infrastructure.client.feign.request.DecreaseStockFeignRequest;
import com.nowayback.order.infrastructure.client.feign.response.DecreaseStockFeignResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient {

    private final ProductFeignClient productFeignClient;

    @Override
    public DecreaseStockResponse decreaseStocks(DecreaseStockRequest request) {
        DecreaseStockFeignResponse response = productFeignClient.decreaseStocks(
            DecreaseStockFeignRequest.from(request));

        return response.toApplicationResponse();
    }
}
