package com.nowayback.order.product.application;

import com.nowayback.order.product.application.command.DecreaseStockCommand;
import com.nowayback.order.product.domain.entity.Product;
import com.nowayback.order.product.domain.repository.ProductRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void decreaseStock(DecreaseStockCommand command) {
        List<UUID> productIds = command.items().stream()
            .map(DecreaseStockCommand.StockItem::productId)
            .toList();

        List<Product> products = productRepository.findAllById(productIds);

        Map<UUID, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getId, p -> p));

        command.items().forEach(item -> {
            UUID productId = item.productId();
            int quantity = item.quantity();

            Product product = productMap.get(productId);
            if (product == null) {
                throw new IllegalArgumentException("상품을 찾을 수 없습니다. productId=" + productId);
            }

            product.decreaseStock(quantity);

            log.info("재고 차감 완료 - 상품 ID: {}, 차감 수량: {}", productId, quantity);
        });
    }
}
