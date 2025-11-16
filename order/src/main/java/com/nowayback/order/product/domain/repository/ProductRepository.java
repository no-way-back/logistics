package com.nowayback.order.product.domain.repository;

import com.nowayback.order.product.domain.entity.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Optional<Product> findById(UUID id);

    Product save(Product product);

    List<Product> findAllById(List<UUID> productIds);
}
