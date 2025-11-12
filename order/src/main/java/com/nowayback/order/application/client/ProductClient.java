package com.nowayback.order.application.client;

import com.nowayback.order.application.client.request.DecreaseStockRequest;
import com.nowayback.order.application.client.request.RestoreStockRequest;
import com.nowayback.order.application.client.response.DecreaseStockResponse;
import com.nowayback.order.application.client.response.RestoreStockResponse;

public interface ProductClient {
    DecreaseStockResponse decreaseStocks(DecreaseStockRequest request);

    RestoreStockResponse restoreStocks(RestoreStockRequest request);
}
