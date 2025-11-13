package com.nowayback.product.presentation.product;

import com.nowayback.common.security.annotation.CurrentUser;
import com.nowayback.common.security.annotation.RequireRole;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.product.application.product.ProductService;
import com.nowayback.product.application.product.command.CreateProductCommand;
import com.nowayback.product.presentation.product.dto.request.CreateProductRequest;
import com.nowayback.product.presentation.product.dto.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER})
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request
    ) {
        CreateProductCommand command = CreateProductCommand.of(
                request.supplierId(),
                request.hubId(),
                request.name(),
                request.price()
        );

        ProductResponse response = ProductResponse.from(productService.createProduct(command));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{productId}")
    @RequireRole({UserRole.MASTER, UserRole.HUB_MANAGER})
    public ResponseEntity<Void> deleteProduct(
            @CurrentUser UUID userId,
            @PathVariable("productId") UUID productId
    ) {
        productService.deleteProduct(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
