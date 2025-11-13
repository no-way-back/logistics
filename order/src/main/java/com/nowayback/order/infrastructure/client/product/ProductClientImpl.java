package com.nowayback.order.infrastructure.client.product;

import com.nowayback.order.application.client.ProductClient;
import com.nowayback.order.application.client.request.DecreaseStockRequest;
import com.nowayback.order.application.client.request.RestoreStockRequest;
import com.nowayback.order.application.client.response.DecreaseStockResponse;
import com.nowayback.order.application.client.response.RestoreStockResponse;
import com.nowayback.order.infrastructure.client.product.feign.ProductFeignClient;
import com.nowayback.order.infrastructure.client.product.feign.request.DecreaseStockFeignRequest;
import com.nowayback.order.infrastructure.client.product.feign.request.RestoreStockFeignRequest;
import com.nowayback.order.infrastructure.client.product.feign.response.DecreaseStockFeignResponse;
import com.nowayback.order.infrastructure.client.product.feign.response.RestoreStockFeignResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient {

    private final ProductFeignClient productFeignClient;

    @Override
    public DecreaseStockResponse decreaseStocks(DecreaseStockRequest request) {
        DecreaseStockFeignResponse response = productFeignClient.decreaseStocks(
            DecreaseStockFeignRequest.from(request)
        );

        return response.toApplicationResponse();
    }

    @Override
    public RestoreStockResponse restoreStocks(RestoreStockRequest request) {
        RestoreStockFeignResponse response = productFeignClient.restoreStocks(
            RestoreStockFeignRequest.from(request)
        );
        return response.toApplicationResponse();
    }
}
