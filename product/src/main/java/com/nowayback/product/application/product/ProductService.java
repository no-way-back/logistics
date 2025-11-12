package com.nowayback.product.application.product;

import com.nowayback.product.application.product.command.CreateProductCommand;
import com.nowayback.product.application.product.dto.ProductResult;
import com.nowayback.product.application.product.exception.ProductApplicationErrorCode;
import com.nowayback.product.application.product.exception.ProductApplicationException;
import com.nowayback.product.application.stock.StockService;
import com.nowayback.product.application.stock.dto.StockResult;
import com.nowayback.product.domain.product.entity.Product;
import com.nowayback.product.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StockService stockService;

    @Transactional
    public ProductResult createProduct(CreateProductCommand command) {
        Product product = Product.create(
                command.supplierId(),
                command.hubId(),
                command.productInfo()
        );

        Product savedProduct = productRepository.save(product);
        StockResult stockResult = stockService.createStock(savedProduct.getId());

        return ProductResult.from(savedProduct, stockResult.quantity());
    }

    @Transactional
    public void deleteProduct(UUID actorId, UUID productId) {
        Product product = getProductById(productId);
        product.delete(actorId);
        productRepository.save(product);
    }

    private Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductApplicationException(ProductApplicationErrorCode.NOT_FOUND_PRODUCT));
    }
}
