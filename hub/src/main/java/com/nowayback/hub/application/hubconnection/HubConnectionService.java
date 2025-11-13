package com.nowayback.hub.application.hubconnection;

import com.nowayback.hub.application.hub.HubService;
import com.nowayback.hub.application.hubconnection.dto.OptimalRouteResult;
import com.nowayback.hub.domain.hub.entity.Hub;
import com.nowayback.hub.domain.hubconnection.entity.HubConnection;
import com.nowayback.hub.domain.hubconnection.repository.HubConnectionRepository;
import com.nowayback.hub.domain.hubconnection.service.RouteCalculator;
import com.nowayback.hub.domain.hubconnection.vo.OptimalRoute;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HubConnectionService {

    private final HubConnectionRepository hubConnectionRepository;
    private final HubService hubService;
    private final RouteCalculator routeCalculator;

    /**
     * 최적 경로 계산 (조회만 수행, 저장 없음)
     *
     * @param startHubId 출발 허브 ID
     * @param endHubId 도착 허브 ID
     * @return 최적 경로 정보
     */
    @Transactional(readOnly = true)
    public OptimalRouteResult findOptimalRoute(UUID startHubId, UUID endHubId) {

        if (startHubId.equals(endHubId)) {
            Hub hub = hubService.findHubOrThrow(startHubId);
            return OptimalRouteResult.sameHub(hub);
        }

        List<HubConnection> connections = hubConnectionRepository.findAll();

        OptimalRoute route = routeCalculator.calculate(startHubId, endHubId, connections);

        if (route.isEmpty()) {
            throw new IllegalArgumentException(
                    "경로를 찾을 수 없습니다."
            );
        }

        List<Hub> hubs = hubService.getHubsByIds(route.getHubIds());
        Map<UUID, Hub> hubMap = hubs.stream()
                .collect(Collectors.toMap(Hub::getId, Function.identity()));

        return OptimalRouteResult.of(route, hubMap);
    }
}