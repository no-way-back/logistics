package com.nowayback.product.domain.stock.entity;

import com.nowayback.product.domain.stock.exception.StockDomainErrorCode;
import com.nowayback.product.domain.stock.exception.StockDomainException;
import com.nowayback.product.domain.stock.vo.ProductId;
import com.nowayback.product.domain.stock.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.nowayback.product.fixture.StockFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("재고 엔티티 테스트")
class StockTest {

    @Nested
    @DisplayName("재고 생성")
    class CreateStock {

        @Test
        @DisplayName("모든 필드가 정상일 경우 재고 생성에 성공한다.")
        void createStock_Success() {
            /* given */
            ProductId productId = PRODUCT_ID;

            /* when */
            Stock stock = Stock.create(productId);

            /* then */
            assertThat(stock.getProductId()).isEqualTo(productId);
        }

        @Test
        @DisplayName("상품 ID는 null일 수 없다.")
        void createStock_ProductIdNull_Failure() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> Stock.create(null))
                    .isInstanceOf(StockDomainException.class)
                    .hasFieldOrPropertyWithValue("errorCode", StockDomainErrorCode.NULL_PRODUCT_ID_OBJECT);
        }
    }

    @Nested
    @DisplayName("재고 수량 수정")
    class UpdateQuantity {

        @Test
        @DisplayName("정상적인 수량으로 재고 수량 수정에 성공한다.")
        void updateQuantity_Success() {
            /* given */
            Stock stock = createStock();
            Quantity newQuantity = QUANTITY;

            /* when */
            stock.updateQuantity(newQuantity);

            /* then */
            assertThat(stock.getQuantity()).isEqualTo(newQuantity);
        }

        @Test
        @DisplayName("수량은 null일 수 없다.")
        void updateQuantity_QuantityNull_Failure() {
            /* given */
            Stock stock = createStock();

            /* when */
            /* then */
            assertThatThrownBy(() -> stock.updateQuantity(null))
                    .isInstanceOf(StockDomainException.class)
                    .hasFieldOrPropertyWithValue("errorCode", StockDomainErrorCode.NULL_QUANTITY_OBJECT);
        }
    }

    @Nested
    @DisplayName("재고 삭제")
    class DeleteStock {

        @Test
        @DisplayName("정상적인 재고 삭제에 성공한다.")
        void deleteStock_Success() {
            /* given */
            Stock stock = createStock();
            UUID actorId = UUID.randomUUID();

            /* when */
            stock.delete(actorId);

            /* then */
            assertThat(stock.isDeleted()).isTrue();
            assertThat(stock.getDeletedBy()).isEqualTo(actorId);
        }
    }
}