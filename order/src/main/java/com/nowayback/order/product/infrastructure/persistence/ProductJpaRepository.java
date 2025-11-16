package com.nowayback.order.product.infrastructure.persistence;

import com.nowayback.order.product.domain.entity.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, UUID> {

}
