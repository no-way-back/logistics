package com.nowayback.delivery.infrastructure.repository;

import com.nowayback.delivery.domain.deliverymanager.entity.DeliveryManager;
import com.nowayback.delivery.domain.deliverymanager.repository.DeliveryManagerRepository;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliverySequence;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.nowayback.delivery.fixture.DeliveryManagerFixture.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(DeliveryManagerRepositoryImpl.class)
@EnableJpaAuditing
@Testcontainers
@DisplayName("배송 담당자 리포지토리")
class DeliveryManagerRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgre = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    private DeliveryManagerRepository deliveryManagerRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Nested
    @DisplayName("배송 담당자 저장")
    class Save {

        @Test
        @DisplayName("저장 시 정상적으로 저장된다.")
        void save_Success() {
            /* given */
            DeliveryManager deliveryManager = createDeliveryManager();;

            /* when */
            DeliveryManager savedDeliveryManager = deliveryManagerRepository.save(deliveryManager);

            /* then */
            assertThat(savedDeliveryManager.getId()).isNotNull();
        }
    }

    @Nested
    @DisplayName("배송 담당자 ID에 대한 존재 여부 확인")
    class ExistsById {

        @Test
        @DisplayName("존재하는 배송 담당자 ID인 경우 true를 반환한다.")
        void existsById_WhenExists_ReturnsTrue() {
            /* given */
            DeliveryManager deliveryManager = createDeliveryManager();

            entityManager.persist(deliveryManager);
            entityManager.flush();

            /* when */
            boolean exists = deliveryManagerRepository.existsById(deliveryManager.getId());

            /* then */
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 배송 담당자 ID인 경우 false를 반환한다.")
        void existsById_WhenNotExists_ReturnsFalse() {
            /* given */
            /* when */
            boolean exists = deliveryManagerRepository.existsById(UUID.randomUUID());

            /* then */
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("배송 담당자 타입에 따른 최대 시퀀스 조회")
    class FindMaxSequenceByType {

        @Test
        @DisplayName("배송 담당자 타입에 따른 최대 시퀀스를 조회한다.")
        void findMaxSequenceByType_Success() {
            /* given */
            UUID deliveryManagerId1 = UUID.randomUUID();
            UUID deliveryManagerId2 = UUID.randomUUID();

            DeliverySequence sequence1 = DeliverySequence.of(1);
            DeliverySequence sequence2 = DeliverySequence.of(2);

            DeliveryManager deliveryManager1 = createDeliveryManager(deliveryManagerId1, MANAGER_TYPE, sequence1);
            DeliveryManager deliveryManager2 = createDeliveryManager(deliveryManagerId2, MANAGER_TYPE, sequence2);

            entityManager.persist(deliveryManager1);
            entityManager.persist(deliveryManager2);
            entityManager.flush();

            /* when */
            Integer maxSequence = deliveryManagerRepository.findMaxSequenceByType(MANAGER_TYPE);

            /* then */
            assertThat(maxSequence).isEqualTo(2);
        }

        @Test
        @DisplayName("해당 타입의 배송 담당자가 없는 경우 0을 반환한다.")
        void findMaxSequenceByType_WhenNoManagers_ReturnsNull() {
            /* given */
            /* when */
            Integer maxSequence = deliveryManagerRepository.findMaxSequenceByType(MANAGER_TYPE);

            /* then */
            assertThat(maxSequence).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("배송 담당자 ID로 배송 담당자 조회")
    class FindById {

        @Test
        @DisplayName("배송 담당자 ID로 배송 담당자를 조회한다.")
        void findById_Success() {
            /* given */
            DeliveryManager deliveryManager = createDeliveryManager();

            entityManager.persist(deliveryManager);
            entityManager.flush();

            /* when */
            DeliveryManager foundDeliveryManager = deliveryManagerRepository.findById(deliveryManager.getId()).orElseThrow();

            /* then */
            assertThat(foundDeliveryManager).isNotNull();
            assertThat(foundDeliveryManager.getId()).isEqualTo(deliveryManager.getId());
        }

        @Test
        @DisplayName("존재하지 않는 배송 담당자 ID로 조회 시 빈 Optional을 반환한다.")
        void findById_WhenNotExists_ReturnsEmptyOptional() {
            /* given */
            /* when */
            Optional<DeliveryManager> foundDeliveryManager = deliveryManagerRepository.findById(UUID.randomUUID());

            /* then */
            assertThat(foundDeliveryManager).isNotPresent();
        }
    }

    @Nested
    @DisplayName("배송 담당자 타입에 따른 모든 배송 담당자 조회")
    class FindAllByTypeOrderBySequenceAsc {

        @Test
        @DisplayName("배송 담당자 타입에 따른 모든 배송 담당자를 시퀀스 오름차순으로 조회한다.")
        void findAllByTypeOrderBySequenceAsc_Success() {
            /* given */
            UUID deliveryManagerId1 = UUID.randomUUID();
            UUID deliveryManagerId2 = UUID.randomUUID();
            UUID deliveryManagerId3 = UUID.randomUUID();

            DeliverySequence sequence1 = DeliverySequence.of(2);
            DeliverySequence sequence2 = DeliverySequence.of(1);
            DeliverySequence sequence3 = DeliverySequence.of(3);

            DeliveryManager deliveryManager1 = createDeliveryManager(deliveryManagerId1, MANAGER_TYPE, sequence1);
            DeliveryManager deliveryManager2 = createDeliveryManager(deliveryManagerId2, MANAGER_TYPE, sequence2);
            DeliveryManager deletedDeliveryManager = createDeliveryManager(deliveryManagerId3, MANAGER_TYPE, sequence3);
            deletedDeliveryManager.delete(UUID.randomUUID());

            entityManager.persist(deliveryManager1);
            entityManager.persist(deliveryManager2);
            entityManager.persist(deletedDeliveryManager);
            entityManager.flush();

            /* when */
            List<DeliveryManager> deliveryManagers = deliveryManagerRepository.findAllByTypeOrderBySequenceAsc(MANAGER_TYPE);

            /* then */
            assertThat(deliveryManagers).hasSize(2);
            assertThat(deliveryManagers.get(0).getDeliverySequence().getSequence()).isEqualTo(1);
            assertThat(deliveryManagers.get(1).getDeliverySequence().getSequence()).isEqualTo(2);
        }

        @Test
        @DisplayName("해당 타입의 배송 담당자가 없는 경우 빈 리스트를 반환한다.")
        void findAllByTypeOrderBySequenceAsc_WhenNoManagers_ReturnsEmptyList() {
            /* given */
            /* when */
            List<DeliveryManager> deliveryManagers = deliveryManagerRepository.findAllByTypeOrderBySequenceAsc(MANAGER_TYPE);

            /* then */
            assertThat(deliveryManagers).isEmpty();
        }
    }
}