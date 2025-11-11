package com.nowayback.delivery.infrastructure.repository;

import com.nowayback.delivery.domain.deliveryroute.entity.DeliveryRoute;
import com.nowayback.delivery.domain.deliveryroute.repository.DeliveryRouteRepository;
import com.nowayback.delivery.domain.deliveryroute.vo.DeliveryId;
import com.nowayback.delivery.domain.deliveryroute.vo.RouteSequence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.nowayback.delivery.fixture.DeliveryRouteFixture.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(DeliveryRouteRepositoryImpl.class)
@EnableJpaAuditing
@Testcontainers
@DisplayName("배송 경로 리포지토리")
class DeliveryRouteRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgre = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    private DeliveryRouteRepository deliveryRouteRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Nested
    @DisplayName("배송 경로 모두 저장")
    class SaveAll {

        @Test
        @DisplayName("배송 경로들을 모두 저장한다.")
        void saveAll_ShouldPersistAllDeliveryRoutes() {
            /* given */
            List<DeliveryRoute> routes = List.of(
                createDeliveryRoute()
            );

            /* when */
            List<DeliveryRoute> saved = deliveryRouteRepository.saveAll(routes);

            /* then */
            for (DeliveryRoute route : saved) {
                assertThat(route.getId()).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("배송 경로 ID에 대한 배송 경로 조회")
    class FindById {

        @Test
        @DisplayName("배송 경로 ID로 배송 경로를 조회한다.")
        void findById_ExistingId_ShouldReturnDeliveryRoute() {
            /* given */
            DeliveryRoute route = createDeliveryRoute();

            entityManager.persist(route);
            entityManager.flush();

            /* when */
            Optional<DeliveryRoute> foundRoute = deliveryRouteRepository.findById(route.getId());

            /* then */
            assertThat(foundRoute).isPresent();
        }

        @Test
        @DisplayName("배송 경로 ID에 대한 배송 경로가 존재하지 않으면 빈 Optional을 반환한다.")
        void findById_NonExistentId_ShouldReturnEmptyOptional() {
            /* given */
            /* when */
            Optional<DeliveryRoute> foundRoute = deliveryRouteRepository.findById(DELIVERY_ROUTE_UUID);

            /* then */
            assertThat(foundRoute).isNotPresent();
        }

        @Test
        @DisplayName("배송 경로 ID에 대한 배송 경로가 삭제된 경우 빈 Optional을 반환한다.")
        void findById_DeletedRoute_ShouldReturnEmptyOptional() {
            /* given */
            DeliveryRoute deletedRoute = createDeliveryRoute();
            deletedRoute.delete(UUID.randomUUID());

            entityManager.persist(deletedRoute);
            entityManager.flush();

            /* when */
            Optional<DeliveryRoute> foundRoute = deliveryRouteRepository.findById(deletedRoute.getId());

            /* then */
            assertThat(foundRoute).isNotPresent();
        }
    }

    @Nested
    @DisplayName("배송 경로 검색")
    class SearchDeliveryRoutes {

        @Test
        @DisplayName("조건에 맞는 배송 경로들을 반환한다.")
        void searchDeliveryRoutes_Filtered_ShouldReturnMatchingRoutes() {
            /* given */
            DeliveryId deliveryId1 = DeliveryId.of(UUID.randomUUID());
            DeliveryId deliveryId2 = DeliveryId.of(UUID.randomUUID());

            RouteSequence routeSequence1 = RouteSequence.of(1);
            RouteSequence routeSequence2 = RouteSequence.of(2);

            DeliveryRoute route1 = createDeliveryRoute(deliveryId1, routeSequence1);
            DeliveryRoute route2 = createDeliveryRoute(deliveryId1, routeSequence2);
            DeliveryRoute route3 = createDeliveryRoute(deliveryId2, routeSequence1);

            entityManager.persist(route1);
            entityManager.persist(route2);
            entityManager.persist(route3);
            entityManager.flush();

            Pageable pageable = PageRequest.of(0, 10, Sort.by("sequence").ascending());

            /* when */
            Page<DeliveryRoute> result = deliveryRouteRepository.findAllByDeliveryId(deliveryId1, pageable);

            /* then */
            assertThat(result.getTotalElements()).isEqualTo(2);

            for (DeliveryRoute route : result.getContent()) {
                assertThat(route.getDeliveryId()).isEqualTo(deliveryId1);
            }

            if (result.getContent().size() > 1) {
                assertThat(result.getContent())
                        .isSortedAccordingTo(Comparator.comparing(route -> route.getSequence().getSequence()));
            }
        }

        @Test
        @DisplayName("조건이 없으면 모든 활성 배송 경로들을 반환한다.")
        void searchDeliveryRoutes_NoFilters_ShouldReturnAllActiveRoutes() {
            /* given */
            DeliveryId deliveryId1 = DeliveryId.of(UUID.randomUUID());
            DeliveryId deliveryId2 = DeliveryId.of(UUID.randomUUID());
            DeliveryId deliveryId3 = DeliveryId.of(UUID.randomUUID());

            RouteSequence routeSequence = RouteSequence.of(1);

            DeliveryRoute route1 = createDeliveryRoute(deliveryId1, routeSequence);
            DeliveryRoute route2 = createDeliveryRoute(deliveryId2, routeSequence);
            DeliveryRoute route3 = createDeliveryRoute(deliveryId3, routeSequence);
            DeliveryRoute deletedRoute = createDeliveryRoute(deliveryId1, routeSequence);
            deletedRoute.delete(UUID.randomUUID());

            entityManager.persist(route1);
            entityManager.persist(route2);
            entityManager.persist(route3);
            entityManager.persist(deletedRoute);
            entityManager.flush();

            Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

            /* when */
            Page<DeliveryRoute> result = deliveryRouteRepository.findAllByDeliveryId(null, pageable);

            /* then */
            assertThat(result.getTotalElements()).isEqualTo(3);
            assertThat(result.getContent()).noneMatch(r -> r.getDeletedAt() != null);

            if (result.getContent().size() > 1) {
                assertThat(result.getContent())
                        .isSortedAccordingTo(Comparator.comparing(DeliveryRoute::getCreatedAt).reversed());
            }
        }

        @Test
        @DisplayName("조건에 맞는 배송 경로가 없으면 빈 페이지를 반환한다.")
        void searchDeliveryRoutes_NoMatchingRoutes_ShouldReturnEmptyPage() {
            /* given */
            Pageable pageable = PageRequest.of(0, 10, Sort.by("sequence").ascending());

            /* when */
            Page<DeliveryRoute> result = deliveryRouteRepository.findAllByDeliveryId(DELIVERY_ID, pageable);

            /* then */
            assertThat(result.getContent()).isEmpty();
        }
    }
}