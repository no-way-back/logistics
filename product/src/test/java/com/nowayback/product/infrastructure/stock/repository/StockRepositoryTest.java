package com.nowayback.product.infrastructure.stock.repository;

import com.nowayback.product.domain.stock.entity.Stock;
import com.nowayback.product.domain.stock.repository.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static com.nowayback.product.fixture.StockFixture.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(StockRepositoryImpl.class)
@EnableJpaAuditing
@Testcontainers
@DisplayName("재고 리포지토리 테스트")
class StockRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgre = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Nested
    @DisplayName("재고 저장")
    class Save {

        @Test
        @DisplayName("정상적으로 재고가 저장된다.")
        void save_Success() {
            /* given */
            Stock stock = createStock();

            /* when */
            Stock savedStock = stockRepository.save(stock);

            /* then */
            assertThat(savedStock.getId()).isNotNull();
        }
    }

    @Nested
    @DisplayName("상품 ID로 재고 존재 여부 확인")
    class ExistsByProductId {

        @Test
        @DisplayName("상품 ID에 대한 재고가 존재하면 true를 반환한다.")
        void existsByProductId_Exists_ShouldReturnTrue() {
            /* given */
            Stock stock = createStock();

            entityManager.persist(stock);
            entityManager.flush();

            /* when */
            boolean exists = stockRepository.existsByProductId(stock.getProductId());

            /* then */
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("상품 ID에 대한 재고가 존재하지 않으면 false를 반환한다.")
        void existsByProductId_NotExists_ShouldReturnFalse() {
            /* given */
            /* when */
            boolean exists = stockRepository.existsByProductId(PRODUCT_ID);

            /* then */
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("상품 아이디로 재고 조회")
    class FindByProductId {

        @Test
        @DisplayName("상품 ID에 대한 재고가 존재하면 재고를 반환한다.")
        void findByProductId_Exists_ShouldReturnStock() {
            /* given */
            Stock stock = createStock();

            entityManager.persist(stock);
            entityManager.flush();

            /* when */
            Optional<Stock> foundStock = stockRepository.findByProductId(stock.getProductId());

            /* then */
            assertThat(foundStock).isPresent();
        }

        @Test
        @DisplayName("상품 ID에 대한 재고가 존재하지 않으면 빈 Optional을 반환한다.")
        void findByProductId_NotExists_ShouldReturnEmptyOptional() {
            /* given */
            /* when */
            Optional<Stock> foundStock = stockRepository.findByProductId(PRODUCT_ID);

            /* then */
            assertThat(foundStock).isNotPresent();
        }

        @Test
        @DisplayName("상품 ID에 대한 재고가 삭제된 경우 빈 Optional을 반환한다.")
        void findByProductId_Deleted_ShouldReturnEmptyOptional() {
            /* given */
            UUID deletedBy = UUID.randomUUID();
            Stock stock = createStock();
            stock.delete(deletedBy);

            entityManager.persist(stock);
            entityManager.flush();

            /* when */
            Optional<Stock> foundStock = stockRepository.findByProductId(stock.getProductId());

            /* then */
            assertThat(foundStock).isNotPresent();
        }
    }
}