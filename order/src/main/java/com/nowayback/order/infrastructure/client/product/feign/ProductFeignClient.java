package com.nowayback.order.infrastructure.client.product.feign;

import com.nowayback.order.infrastructure.client.product.feign.request.DecreaseStockFeignRequest;
import com.nowayback.order.infrastructure.client.product.feign.request.RestoreStockFeignRequest;
import com.nowayback.order.infrastructure.client.product.feign.response.DecreaseStockFeignResponse;
import com.nowayback.order.infrastructure.client.product.feign.response.RestoreStockFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

    @PatchMapping("/products/stocks")
    DecreaseStockFeignResponse decreaseStocks(
        @RequestBody DecreaseStockFeignRequest request);

    @PatchMapping("/products/stocks/restore")
    RestoreStockFeignResponse restoreStocks(
        @RequestBody RestoreStockFeignRequest request);
}
