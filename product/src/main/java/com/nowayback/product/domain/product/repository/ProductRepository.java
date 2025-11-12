package com.nowayback.product.domain.product.repository;

import com.nowayback.product.domain.product.entity.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(UUID productId);
}
