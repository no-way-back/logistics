package com.nowayback.order.product.infrastructure.persistence;


import com.nowayback.order.product.domain.entity.Product;
import com.nowayback.order.product.domain.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Optional<Product> findById(UUID id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public List<Product> findAllById(List<UUID> productIds) {
        return productJpaRepository.findAllById(productIds);
    }
}
