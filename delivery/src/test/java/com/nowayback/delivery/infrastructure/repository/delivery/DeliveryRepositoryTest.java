package com.nowayback.delivery.infrastructure.repository.delivery;

import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.repository.DeliveryRepository;
import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;
import com.nowayback.delivery.domain.delivery.vo.HubId;
import com.nowayback.delivery.domain.delivery.vo.OrderId;
import com.nowayback.delivery.infrastructure.config.QueryDslConfig;
import com.nowayback.delivery.infrastructure.delivery.DeliveryCustomRepositoryImpl;
import com.nowayback.delivery.infrastructure.delivery.DeliveryRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

import static com.nowayback.delivery.fixture.DeliveryFixture.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DeliveryRepositoryImpl.class, DeliveryCustomRepositoryImpl.class, QueryDslConfig.class})
@EnableJpaAuditing
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

    @Nested
    @DisplayName("배송 검색")
    class SearchDeliveries {

        @Test
        @DisplayName("조건에 맞는 배송들을 반환한다.")
        void searchDeliveries_shouldReturnMatchingDeliveries() {
            /* given */
            HubId sourceHubId = HubId.of(UUID.randomUUID());
            HubId destinationHubId = HubId.of(UUID.randomUUID());
            OrderId orderId1 = OrderId.of(UUID.randomUUID());
            OrderId orderId2 = OrderId.of(UUID.randomUUID());
            OrderId orderId3 = OrderId.of(UUID.randomUUID());

            Delivery delivery1 = createDelivery(orderId1, sourceHubId, destinationHubId, DeliveryStatus.WAITING_AT_HUB);
            Delivery delivery2 = createDelivery(orderId2, sourceHubId, destinationHubId, DeliveryStatus.DELIVERED);
            Delivery delivery3 = createDelivery(orderId3, sourceHubId, destinationHubId, DeliveryStatus.WAITING_AT_HUB);
            Delivery deletedDelivery = createDelivery();
            deletedDelivery.delete(UUID.randomUUID());

            entityManager.persist(delivery1);
            entityManager.persist(delivery2);
            entityManager.persist(delivery3);
            entityManager.persist(deletedDelivery);
            entityManager.flush();

            /* when */
            Page<Delivery> result = deliveryRepository.searchDeliveries(null, sourceHubId, destinationHubId, DeliveryStatus.WAITING_AT_HUB, PAGE, SIZE);

            /* then */
            assertThat(result.getContent()).hasSize(2);

            assertThat(result.getContent()).noneMatch(d -> d.getDeletedAt() != null);

            if (result.getContent().size() > 1) {
                assertThat(result.getContent())
                        .isSortedAccordingTo(Comparator.comparing(Delivery::getCreatedAt).reversed());
            }
        }

        @Test
        @DisplayName("조건이 없으면 모든 활성 배송들을 반환한다.")
        void searchDeliveries_shouldReturnAllActiveDeliveriesWhenNoCondition() {
            /* given */
            OrderId orderId1 = OrderId.of(UUID.randomUUID());
            OrderId orderId2 = OrderId.of(UUID.randomUUID());
            OrderId orderId3 = OrderId.of(UUID.randomUUID());

            HubId sourceHubId = SOURCE_HUB_ID;
            HubId destinationHubId = DESTINATION_HUB_ID;
            DeliveryStatus status = DELIVERY_STATUS;

            Delivery delivery1 = createDelivery(orderId1, sourceHubId, destinationHubId, status);
            Delivery delivery2 = createDelivery(orderId2, sourceHubId, destinationHubId, status);
            Delivery delivery3 = createDelivery(orderId3, sourceHubId, destinationHubId, status);

            entityManager.persist(delivery1);
            entityManager.persist(delivery2);
            entityManager.persist(delivery3);
            entityManager.flush();;

            /* when */
            Page<Delivery> result = deliveryRepository.searchDeliveries(null, null, null, null, PAGE, SIZE);

            /* then */
            assertThat(result.getContent()).hasSize(3);
            assertThat(result.getContent()).noneMatch(d -> d.getDeletedAt() != null);
        }

        @Test
        @DisplayName("조건에 맞는 배송이 없으면 빈 페이지를 반환한다.")
        void searchDeliveries_shouldReturnEmptyPageIfNoMatchingDeliveries() {
            /* given */
            /* when */
            Page<Delivery> result = deliveryRepository.searchDeliveries(ORDER_ID, null, null, null, PAGE, SIZE);

            /* then */
            assertThat(result.getContent()).isEmpty();
        }
    }
}