package com.nowayback.product.domain.product.entity;

import com.nowayback.product.domain.product.exception.ProductDomainErrorCode;
import com.nowayback.product.domain.product.exception.ProductDomainException;
import com.nowayback.product.domain.product.vo.CompanyId;
import com.nowayback.product.domain.product.vo.HubId;
import com.nowayback.product.domain.product.vo.ProductInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.nowayback.product.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품 엔티티 테스트")
class ProductTest {

    @Nested
    @DisplayName("상품 생성")
    class CreateProduct {

        @Test
        @DisplayName("모든 필드가 정상일 경우 상품 생성에 성공한다.")
        void createProduct_Success() {
            /* given */
            CompanyId supplierId = SUPPLIER_ID;
            HubId hubId = HUB_ID;
            ProductInfo productInfo = PRODUCT_INFO;

            /* when */
            Product product = Product.create(
                    supplierId,
                    hubId,
                    productInfo
            );

            /* then */
            assertThat(product.getSupplierId()).isEqualTo(supplierId);
            assertThat(product.getHubId()).isEqualTo(hubId);
            assertThat(product.getProductInfo()).isEqualTo(productInfo);
        }

        @Test
        @DisplayName("공급업체 ID는 null일 수 없다.")
        void createProduct_SupplierIdNull_Failure() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> Product.create(null, HUB_ID, PRODUCT_INFO))
                    .isInstanceOf(ProductDomainException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ProductDomainErrorCode.NULL_SUPPLIER_ID_OBJECT);
        }

        @Test
        @DisplayName("허브 ID는 null일 수 없다.")
        void createProduct_HubIdNull_Failure() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> Product.create(SUPPLIER_ID, null, PRODUCT_INFO))
                    .isInstanceOf(ProductDomainException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ProductDomainErrorCode.NULL_HUB_ID_OBJECT);
        }

        @Test
        @DisplayName("상품 정보는 null일 수 없다.")
        void createProduct_ProductInfoNull_Failure() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> Product.create(SUPPLIER_ID, HUB_ID, null))
                    .isInstanceOf(ProductDomainException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ProductDomainErrorCode.NULL_PRODUCT_INFO_OBJECT);
        }
    }

    @Nested
    @DisplayName("상품 삭제")
    class DeleteProduct {

        @Test
        @DisplayName("정상적인 상품 삭제에 성공한다.")
        void deleteProduct_Success() {
            /* given */
            Product product = createProduct();
            UUID deletedBy = UUID.randomUUID();

            /* when */
            product.delete(deletedBy);

            /* then */
            assertThat(product.isDeleted()).isTrue();
            assertThat(product.getDeletedBy()).isEqualTo(deletedBy);
        }
    }
}