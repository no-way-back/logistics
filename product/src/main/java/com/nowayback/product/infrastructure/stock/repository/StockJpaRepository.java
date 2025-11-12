package com.nowayback.product.infrastructure.stock.repository;

import com.nowayback.product.domain.stock.entity.Stock;
import com.nowayback.product.domain.stock.vo.ProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StockJpaRepository extends JpaRepository<Stock, UUID> {
    boolean existsByProductIdAndDeletedAtIsNull(ProductId productId);
    Optional<Stock> findByProductIdAndDeletedAtIsNull(ProductId productId);
}
