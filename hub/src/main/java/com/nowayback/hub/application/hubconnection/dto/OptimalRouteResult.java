package com.nowayback.hub.application.hubconnection.dto;

import com.nowayback.hub.domain.hub.entity.Hub;
import com.nowayback.hub.domain.hubconnection.vo.OptimalRoute;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class OptimalRouteResult {

    private final UUID originHubId;
    private final String originHubName;
    private final UUID destinationHubId;
    private final String destinationHubName;
    private final Integer totalDistanceM;
    private final Integer totalDurationMin;
    private final List<RouteSegment> segments;

    public static OptimalRouteResult of(OptimalRoute route, Map<UUID, Hub> hubMap) {
        List<UUID> hubIds = route.getHubIds();

        // 세그먼트 생성
        List<RouteSegment> segments = new ArrayList<>();
        for (int i = 0; i < hubIds.size() - 1; i++) {
            Hub fromHub = hubMap.get(hubIds.get(i));
            Hub toHub = hubMap.get(hubIds.get(i + 1));

            segments.add(RouteSegment.of(
                    i + 1,
                    fromHub,
                    toHub,
                    route.getSegmentDistance(i),
                    route.getSegmentDuration(i)
            ));
        }

        Hub originHub = hubMap.get(hubIds.get(0));
        Hub destinationHub = hubMap.get(hubIds.get(hubIds.size() - 1));

        return new OptimalRouteResult(
                originHub.getId(),
                originHub.getName(),
                destinationHub.getId(),
                destinationHub.getName(),
                route.getTotalDistanceM(),
                route.getTotalDurationMin(),
                segments
        );
    }

    public static OptimalRouteResult sameHub(Hub hub) {
        return new OptimalRouteResult(
                hub.getId(),
                hub.getName(),
                hub.getId(),
                hub.getName(),
                0,
                0,
                List.of()
        );
    }

    @Getter
    public static class RouteSegment {
        private final Integer sequence;
        private final UUID fromHubId;
        private final String fromHubName;
        private final UUID toHubId;
        private final String toHubName;
        private final Integer distanceKm;
        private final Integer estimatedDurationMin;

        private RouteSegment(
                Integer sequence,
                UUID fromHubId,
                String fromHubName,
                UUID toHubId,
                String toHubName,
                Integer distanceKm,
                Integer estimatedDurationMin
        ) {
            this.sequence = sequence;
            this.fromHubId = fromHubId;
            this.fromHubName = fromHubName;
            this.toHubId = toHubId;
            this.toHubName = toHubName;
            this.distanceKm = distanceKm;
            this.estimatedDurationMin = estimatedDurationMin;
        }

        public static RouteSegment of(
                Integer sequence,
                Hub fromHub,
                Hub toHub,
                Integer distanceM,
                Integer durationMin
        ) {
            return new RouteSegment(
                    sequence,
                    fromHub.getId(),
                    fromHub.getName(),
                    toHub.getId(),
                    toHub.getName(),
                    distanceM / 1000,
                    durationMin
            );
        }
    }

    private OptimalRouteResult(
            UUID originHubId,
            String originHubName,
            UUID destinationHubId,
            String destinationHubName,
            Integer totalDistanceM,
            Integer totalDurationMin,
            List<RouteSegment> segments
    ) {
        this.originHubId = originHubId;
        this.originHubName = originHubName;
        this.destinationHubId = destinationHubId;
        this.destinationHubName = destinationHubName;
        this.totalDistanceM = totalDistanceM;
        this.totalDurationMin = totalDurationMin;
        this.segments = segments;
    }
}