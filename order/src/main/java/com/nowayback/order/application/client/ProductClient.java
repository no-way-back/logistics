package com.nowayback.order.application.client;

import com.nowayback.order.application.client.request.DecreaseStockRequest;
import com.nowayback.order.application.client.response.DecreaseStockResponse;

public interface ProductClient {
    DecreaseStockResponse decreaseStocks(DecreaseStockRequest request);
}
