package com.nowayback.product.application.stock;

import com.nowayback.product.application.stock.command.DecreaseStockCommand;
import com.nowayback.product.application.stock.command.IncreaseStockCommand;
import com.nowayback.product.application.stock.dto.StockResult;
import com.nowayback.product.application.stock.exception.StockApplicationErrorCode;
import com.nowayback.product.application.stock.exception.StockApplicationException;
import com.nowayback.product.domain.stock.entity.Stock;
import com.nowayback.product.domain.stock.exception.StockDomainErrorCode;
import com.nowayback.product.domain.stock.exception.StockDomainException;
import com.nowayback.product.domain.stock.repository.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.nowayback.product.fixture.StockFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    @Nested
    @DisplayName("재고 생성")
    class CreateStock {

        @Test
        @DisplayName("정상적으로 재고가 생성된다.")
        void createStock_Success() {
            /* given */
            UUID productId = PRODUCT_UUID;

            when(stockRepository.existsByProductId(any())).thenReturn(false);
            when(stockRepository.save(any())).thenReturn(createStock());

            /* when */
            StockResult result = stockService.createStock(productId);

            /* then */
            assertThat(result.productId()).isEqualTo(productId);
            assertThat(result.quantity()).isZero();
        }

        @Test
        @DisplayName("이미 존재하는 상품에 대해 재고를 생성하려고 하면 예외가 발생한다.")
        void createStock_DuplicateProductId_Exception() {
            /* given */
            UUID productId = PRODUCT_UUID;

            when(stockRepository.existsByProductId(any())).thenReturn(true);

            /* when */
            /* then */
            assertThatThrownBy(() -> stockService.createStock(productId))
                    .isInstanceOf(StockApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", StockApplicationErrorCode.DUPLICATE_PRODUCT_ID);
        }
    }

    @Nested
    @DisplayName("재고 조회")
    class GetStock {

        @Test
        @DisplayName("정상적으로 재고가 조회된다.")
        void getStock_Success() {
            /* given */
            Stock stock = createStock();
            UUID productId = PRODUCT_UUID;

            when(stockRepository.findByProductId(any())).thenReturn(Optional.of(stock));

            /* when */
            StockResult result = stockService.getStock(productId);

            /* then */
            assertThat(result.productId()).isEqualTo(productId);
            assertThat(result.quantity()).isZero();
        }

        @Test
        @DisplayName("존재하지 않는 재고를 조회하려고 하면 예외가 발생한다.")
        void getStock_NotFoundStock_Exception() {
            /* given */
            UUID productId = PRODUCT_UUID;

            when(stockRepository.findByProductId(any())).thenReturn(Optional.empty());

            /* when */
            /* then */
            assertThatThrownBy(() -> stockService.getStock(productId))
                    .isInstanceOf(StockApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", StockApplicationErrorCode.NOT_FOUND_STOCK);
        }
    }

    @Nested
    @DisplayName("재고 증가")
    class IncreaseStock {

        @Test
        @DisplayName("정상적으로 재고가 증가된다.")
        void increaseStock_Success() {
            /* given */
            IncreaseStockCommand command = INCREASE_STOCK_COMMAND;
            Stock stock = createStock();

            when(stockRepository.findByProductId(any())).thenReturn(Optional.of(stock));

            /* when */
            StockResult result = stockService.increaseStock(command);

            /* then */
            assertThat(result.productId()).isEqualTo(command.productId().getId());
            assertThat(result.quantity()).isEqualTo(command.amount().getQuantity());
        }

        @Test
        @DisplayName("존재하지 않는 재고에 대해 재고 증가를 시도하면 예외가 발생한다.")
        void increaseStock_NotFoundStock_Exception() {
            /* given */
            IncreaseStockCommand command = INCREASE_STOCK_COMMAND;

            when(stockRepository.findByProductId(any())).thenReturn(Optional.empty());

            /* when */
            /* then */
            assertThatThrownBy(() -> stockService.increaseStock(command))
                    .isInstanceOf(StockApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", StockApplicationErrorCode.NOT_FOUND_STOCK);
        }
    }

    @Nested
    @DisplayName("재고 차감")
    class DecreaseStock {

        @Test
        @DisplayName("정상적으로 재고가 차감된다.")
        void decreaseStock_Success() {
            /* given */
            DecreaseStockCommand command = DECREASE_STOCK_COMMAND;
            Stock stock = createStock(QUANTITY);

            when(stockRepository.findByProductId(any())).thenReturn(Optional.of(stock));

            /* when */
            StockResult result = stockService.decreaseStock(command);

            /* then */
            assertThat(result.productId()).isEqualTo(command.productId().getId());
        }

        @Test
        @DisplayName("존재하지 않는 재고에 대해 재고 차감을 시도하면 예외가 발생한다.")
        void decreaseStock_NotFoundStock_Exception() {
            /* given */
            DecreaseStockCommand command = DECREASE_STOCK_COMMAND;

            when(stockRepository.findByProductId(any())).thenReturn(Optional.empty());

            /* when */
            /* then */
            assertThatThrownBy(() -> stockService.decreaseStock(command))
                    .isInstanceOf(StockApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", StockApplicationErrorCode.NOT_FOUND_STOCK);
        }

        @Test
        @DisplayName("재고보다 많은 양을 차감하려고 하면 예외가 발생한다.")
        void decreaseStock_InsufficientStock_Exception() {
            /* given */
            DecreaseStockCommand command = INSUFFICIENT_DECREASE_STOCK_COMMAND;
            Stock stock = createStock(QUANTITY);

            when(stockRepository.findByProductId(any())).thenReturn(Optional.of(stock));

            /* when */
            /* then */
            assertThatThrownBy(() -> stockService.decreaseStock(command))
                    .isInstanceOf(StockDomainException.class)
                    .hasFieldOrPropertyWithValue("errorCode", StockDomainErrorCode.INSUFFICIENT_STOCK_QUANTITY);
        }
    }

    @Nested
    @DisplayName("재고 삭제")
    class DeleteStock {

        @Test
        @DisplayName("정상적으로 재고가 삭제된다.")
        void deleteStock_Success() {
            /* given */
            UUID actorId = UUID.randomUUID();
            UUID productId = PRODUCT_UUID;
            Stock stock = createStock();

            when(stockRepository.findByProductId(any())).thenReturn(Optional.of(stock));

            /* when */
            stockService.deleteStock(actorId, productId);

            /* then */
            assertThat(stock.isDeleted()).isTrue();
            verify(stockRepository).save(any(Stock.class));
        }

        @Test
        @DisplayName("존재하지 않는 재고에 대해 재고 삭제를 시도하면 예외가 발생한다.")
        void deleteStock_NotFoundStock_Exception() {
            /* given */
            UUID actorId = UUID.randomUUID();
            UUID productId = PRODUCT_UUID;

            when(stockRepository.findByProductId(any())).thenReturn(Optional.empty());

            /* when */
            /* then */
            assertThatThrownBy(() -> stockService.deleteStock(actorId, productId))
                    .isInstanceOf(StockApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", StockApplicationErrorCode.NOT_FOUND_STOCK);
        }
    }
}