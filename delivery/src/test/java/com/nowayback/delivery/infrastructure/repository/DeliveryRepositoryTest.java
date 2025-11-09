package com.nowayback.delivery.infrastructure.repository;

import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.repository.DeliveryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static com.nowayback.delivery.fixture.DeliveryFixture.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(DeliveryRepositoryImpl.class)
@Testcontainers
class DeliveryRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgre = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    private DeliveryRepository deliveryRepository;

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
            Delivery savedDelivery = deliveryRepository.save(delivery);

            /* then */
            assertThat(savedDelivery.getId()).isNotNull();
            assertThat(savedDelivery.getOrderId()).isEqualTo(delivery.getOrderId());
        }
    }

    @Nested
    @DisplayName("주문 ID에 대한 배송 존재 여부 확인")
    class ExistsByOrderId {

        @Test
        @DisplayName("주문 ID에 대한 배송이 존재하면 true를 반환한다.")
        void existsByOrderId_shouldReturnTrueIfExists() {
            /* given */
            Delivery activeDelivery = createDelivery();

            entityManager.persist(activeDelivery);
            entityManager.flush();

            /* when */
            boolean exists = deliveryRepository.existsByOrderId(activeDelivery.getOrderId());

            /* then */
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("주문 ID에 대한 배송이 존재하지 않으면 false를 반환한다.")
        void existsByOrderId_shouldReturnFalseIfNotExists() {
            /* given */
            /* when */
            boolean exists = deliveryRepository.existsByOrderId(ORDER_ID);

            /* then */
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("주문 ID에 대한 배송이 삭제된 경우 false를 반환한다.")
        void existsByOrderId_shouldReturnFalseIfDeleted() {
            /* given */
            Delivery deletedDelivery = createDelivery();
            deletedDelivery.delete(UUID.randomUUID());

            entityManager.persist(deletedDelivery);
            entityManager.flush();

            /* when */
            boolean exists = deliveryRepository.existsByOrderId(deletedDelivery.getOrderId());

            /* then */
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("배송 ID에 대한 배송 조회")
    class FindById {

        @Test
        @DisplayName("배송 ID에 대한 배송이 존재하면 배송 객체를 반환한다.")
        void findById_shouldReturnDeliveryIfExists() {
            /* given */
            Delivery delivery = createDelivery();

            entityManager.persist(delivery);
            entityManager.flush();

            /* when */
            Optional<Delivery> foundDelivery = deliveryRepository.findById(delivery.getId());

            /* then */
            assertThat(foundDelivery).isPresent();
        }

        @Test
        @DisplayName("배송 ID에 대한 배송이 존재하지 않으면 빈 Optional을 반환한다.")
        void findById_shouldReturnEmptyIfNotExists() {
            /* given */
            UUID deliveryId = UUID.randomUUID();

            /* when */
            Optional<Delivery> foundDelivery = deliveryRepository.findById(deliveryId);

            /* then */
            assertThat(foundDelivery).isNotPresent();
        }

        @Test
        @DisplayName("배송 ID에 대한 배송이 삭제된 경우 빈 Optional을 반환한다.")
        void findById_shouldReturnEmptyIfDeleted() {
            /* given */
            Delivery deletedDelivery = createDelivery();
            deletedDelivery.delete(UUID.randomUUID());

            entityManager.persist(deletedDelivery);
            entityManager.flush();

            /* when */
            Optional<Delivery> foundDelivery = deliveryRepository.findById(deletedDelivery.getId());

            /* then */
            assertThat(foundDelivery).isNotPresent();
        }
    }
}