package com.nowayback.product.application.product;

import com.nowayback.product.application.product.command.CreateProductCommand;
import com.nowayback.product.application.product.dto.ProductResult;
import com.nowayback.product.application.product.exception.ProductApplicationErrorCode;
import com.nowayback.product.application.product.exception.ProductApplicationException;
import com.nowayback.product.application.stock.StockService;
import com.nowayback.product.domain.product.entity.Product;
import com.nowayback.product.domain.product.repository.ProductRepository;
import com.nowayback.product.fixture.StockFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.nowayback.product.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품 서비스 테스트")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockService stockService;

    @InjectMocks
    private ProductService productService;

    @Nested
    @DisplayName("상품 생성")
    class CreateProduct {

        @Test
        @DisplayName("정상적으로 상품이 생성된다.")
        void createProduct_Success() {
            /* given */
            CreateProductCommand command = CREATE_PRODUCT_COMMAND;

            when(productRepository.save(any())).thenReturn(createProduct());
            when(stockService.createStock(any())).thenReturn(StockFixture.STOCK_RESULT);

            /* when */
            ProductResult result = productService.createProduct(command);

            /* then */
            assertThat(result.supplierId()).isEqualTo(SUPPLIER_UUID);
            assertThat(result.hubId()).isEqualTo(HUB_UUID);
            assertThat(result.name()).isEqualTo(NAME);
            assertThat(result.price()).isEqualTo(PRICE);
            assertThat(result.quantity()).isZero();

            verify(productRepository).save(any());
            verify(stockService).createStock(any());
        }
    }

    @Nested
    @DisplayName("상품 삭제")
    class DeleteProduct {

        @Test
        @DisplayName("정상적으로 상품이 삭제된다.")
        void deleteProduct_Success() {
            /* given */
            UUID actorId = UUID.randomUUID();
            UUID productId = PRODUCT_ID;
            Product product = createProduct();

            when(productRepository.findById(any())).thenReturn(Optional.of(product));

            /* when */
            productService.deleteProduct(actorId, productId);

            /* then */
            assertThat(product.isDeleted()).isTrue();

            verify(productRepository).save(any());
            verify(stockService).deleteStock(actorId, productId);
        }

        @Test
        @DisplayName("존재하지 않는 상품을 삭제하려고 하면 예외가 발생한다.")
        void deleteProduct_NotFoundProduct_Exception() {
            /* given */
            UUID actorId = UUID.randomUUID();
            UUID productId = PRODUCT_ID;

            when(productRepository.findById(any())).thenReturn(Optional.empty());

            /* when */
            /* then */
            assertThatThrownBy(() -> productService.deleteProduct(actorId, productId))
                    .isInstanceOf(ProductApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ProductApplicationErrorCode.NOT_FOUND_PRODUCT);
        }
    }
}