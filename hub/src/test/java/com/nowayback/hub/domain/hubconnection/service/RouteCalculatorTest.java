package com.nowayback.hub.domain.hubconnection.service;

import com.nowayback.hub.domain.hubconnection.entity.HubConnection;
import com.nowayback.hub.domain.hubconnection.vo.OptimalRoute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.nowayback.hub.fixture.HubConnectionFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class RouteCalculatorTest {

    private RouteCalculator routeCalculator;
    private UUID gyeonggiId;
    private UUID chungbukId;
    private UUID gyeongnamId;
    private UUID busanId;

    @BeforeEach
    void setUp() {
        routeCalculator = new RouteCalculator();

        // 허브 ID 설정
        gyeonggiId = UUID.randomUUID();
        chungbukId = UUID.randomUUID();
        gyeongnamId = UUID.randomUUID();
        busanId = UUID.randomUUID();
    }

    @Test
    @DisplayName("경기 -> 충북 -> 경남 -> 부산 최적 경로 계산")
    void calculate_Success() {
        // given
        List<HubConnection> connections = new ArrayList<>();

        connections.add(createMedium(gyeonggiId, chungbukId)); // 100km
        connections.add(createLong(chungbukId, gyeongnamId)); // 200km
        connections.add(createShort(gyeongnamId, busanId)); // 50km
        connections.add(create(gyeonggiId, gyeongnamId, 400000, 240)); // 직통

        // when
        OptimalRoute route = routeCalculator.calculate(gyeonggiId, busanId, connections);

        // then
        assertThat(route.getHubIds()).hasSize(4);
        assertThat(route.getHubIds()).containsExactly(
                gyeonggiId, chungbukId, gyeongnamId, busanId
        );
        assertThat(route.getTotalDistanceM()).isEqualTo(350000); // 100 + 200 + 50
        assertThat(route.getTotalDurationMin()).isEqualTo(210); // 60 + 120 + 30
        assertThat(route.getSegmentDistances()).containsExactly(100000, 200000, 50000);
        assertThat(route.getSegmentDurations()).containsExactly(60, 120, 30);
    }

    @Test
    @DisplayName("경로가 없는 경우 빈 결과 반환")
    void calculate_NoRoute() {
        // given
        List<HubConnection> connections = new ArrayList<>();
        UUID isolatedHubId = UUID.randomUUID();

        connections.add(createMedium(gyeonggiId, chungbukId));

        // when
        OptimalRoute route = routeCalculator.calculate(gyeonggiId, isolatedHubId, connections);

        // then
        assertThat(route.isEmpty()).isTrue();
        assertThat(route.getTotalDistanceM()).isEqualTo(0);
    }

    @Test
    @DisplayName("출발지와 목적지가 같은 경우")
    void calculate_SameHub() {
        // given
        List<HubConnection> connections = new ArrayList<>();

        // when
        OptimalRoute route = routeCalculator.calculate(gyeonggiId, gyeonggiId, connections);

        // then
        assertThat(route.getHubIds()).hasSize(1);
        assertThat(route.getHubIds()).containsExactly(gyeonggiId);
        assertThat(route.getTotalDistanceM()).isEqualTo(0);
        assertThat(route.getTotalDurationMin()).isEqualTo(0);
    }

    @Test
    @DisplayName("여러 경로 중 최단 경로 선택")
    void calculate_MultipleRoutes() {
        // given
        List<HubConnection> connections = new ArrayList<>();

        // 경로 1: 경기 -> 충북 -> 부산 (300km)
        connections.add(createMedium(gyeonggiId, chungbukId)); // 100km
        connections.add(createLong(chungbukId, busanId)); // 200km

        // 경로 2: 경기 -> 경남 -> 부산 (250km) - 더 짧은 경로
        connections.add(create(gyeonggiId, gyeongnamId, 150000, 90)); // 150km
        connections.add(createMedium(gyeongnamId, busanId)); // 100km

        // when
        OptimalRoute route = routeCalculator.calculate(gyeonggiId, busanId, connections);

        // then
        assertThat(route.getHubIds()).containsExactly(
                gyeonggiId, gyeongnamId, busanId
        );
        assertThat(route.getTotalDistanceM()).isEqualTo(250000);
    }
}