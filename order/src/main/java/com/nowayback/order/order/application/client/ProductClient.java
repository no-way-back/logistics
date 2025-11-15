package com.nowayback.order.order.application.client;

import com.nowayback.order.order.application.client.request.DecreaseStockRequest;
import com.nowayback.order.order.application.client.response.DecreaseStockResponse;

public interface ProductClient {
    DecreaseStockResponse decreaseStocks(DecreaseStockRequest request);
}
