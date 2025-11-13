package com.nowayback.product.domain.stock.repository;

import com.nowayback.product.domain.stock.entity.Stock;
import com.nowayback.product.domain.stock.vo.ProductId;

import java.util.Optional;

public interface StockRepository {
    Stock save(Stock stock);
    boolean existsByProductId(ProductId productId);
    Optional<Stock> findByProductId(ProductId productId);
}
