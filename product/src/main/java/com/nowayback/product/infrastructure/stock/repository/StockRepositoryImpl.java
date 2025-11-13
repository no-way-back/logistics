package com.nowayback.product.infrastructure.stock.repository;

import com.nowayback.product.domain.stock.entity.Stock;
import com.nowayback.product.domain.stock.repository.StockRepository;
import com.nowayback.product.domain.stock.vo.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepository {

    private final StockJpaRepository stockJpaRepository;

    @Override
    public Stock save(Stock stock) {
        return stockJpaRepository.save(stock);
    }

    @Override
    public boolean existsByProductId(ProductId productId) {
        return stockJpaRepository.existsByProductIdAndDeletedAtIsNull(productId);
    }

    @Override
    public Optional<Stock> findByProductId(ProductId productId) {
        return stockJpaRepository.findByProductIdAndDeletedAtIsNull(productId);
    }
}
