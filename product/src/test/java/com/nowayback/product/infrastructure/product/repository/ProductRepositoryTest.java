package com.nowayback.product.infrastructure.product.repository;

import com.nowayback.product.domain.product.entity.Product;
import com.nowayback.product.domain.product.repository.ProductRepository;
import com.nowayback.product.infrastructure.stock.repository.StockRepositoryImpl;
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

import static com.nowayback.product.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ProductRepositoryImpl.class)
@EnableJpaAuditing
@Testcontainers
@DisplayName("상품 리포지토리 테스트")
class ProductRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgre = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Nested
    @DisplayName("상품 저장")
    class Save {

        @Test
        @DisplayName("정상적으로 상품이 저장된다.")
        void save_Success() {
            /* given */
            Product product = createProduct();

            /* when */
            Product savedProduct = productRepository.save(product);

            /* then */
            assertThat(savedProduct.getId()).isNotNull();
        }
    }


    @Nested
    @DisplayName("상품 ID로 상품 조회")
    class FindById {

        @Test
        @DisplayName("존재하는 상품 ID로 조회하면 상품을 반환한다.")
        void findById_ExistingProductId_Success() {
            /* given */
            Product product = createProduct();

            entityManager.persist(product);
            entityManager.flush();

            /* when */
            Optional<Product> foundProduct = productRepository.findById(product.getId());

            /* then */
            assertThat(foundProduct).isPresent();
        }

        @Test
        @DisplayName("존재하지 않는 상품 ID로 조회하면 빈 Optional을 반환한다.")
        void findById_NonExistingProductId_ShouldReturnEmptyOptional() {
            /* given */
            /* when */
            Optional<Product> foundProduct = productRepository.findById(PRODUCT_ID);

            /* then */
            assertThat(foundProduct).isNotPresent();
        }

        @Test
        @DisplayName("상품 ID에 대한 상품이 삭제된 경우 빈 Optional을 반환한다.")
        void findById_DeletedProductId_ShouldReturnEmptyOptional() {
            /* given */
            UUID deletedBy = UUID.randomUUID();
            Product product = createProduct();
            product.delete(deletedBy);

            entityManager.persist(product);
            entityManager.flush();

            /* when */
            Optional<Product> foundProduct = productRepository.findById(product.getId());

            /* then */
            assertThat(foundProduct).isNotPresent();
        }
    }
}