package com.nowayback.product.infrastructure.product.repository;

import com.nowayback.product.domain.product.entity.Product;
import com.nowayback.product.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        return productJpaRepository.findByIdAndDeletedAtIsNull(productId);
    }
}
