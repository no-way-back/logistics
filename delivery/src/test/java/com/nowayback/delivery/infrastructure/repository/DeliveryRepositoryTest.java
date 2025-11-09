package com.nowayback.delivery.infrastructure.repository;

import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.fixture.DeliveryFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.nowayback.delivery.fixture.DeliveryFixture.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class DeliveryRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgre = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    private DeliveryJpaRepository deliveryJpaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Nested
    @DisplayName("배송 저장")
    class Save {

        @Test
        @DisplayName("배송 저장 시 정상적으로 저장된다.")
        void save_shouldPersistDelivery() {
            /* given */
            Delivery delivery = createDelivery();

            /* when */
            Delivery savedDelivery = deliveryJpaRepository.save(delivery);

            /* then */
            assertThat(savedDelivery.getId()).isNotNull();
            assertThat(savedDelivery.getOrderId()).isEqualTo(delivery.getOrderId());
        }
    }
}