package com.nowayback.hub.domain.hubconnection.vo;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class OptimalRoute {

    private final List<UUID> hubIds;
    private final List<Integer> segmentDistances;  // 각 구간별 거리
    private final List<Integer> segmentDurations;  // 각 구간별 시간
    private final Integer totalDistanceM;
    private final Integer totalDurationMin;

    private OptimalRoute(
            List<UUID> hubIds,
            List<Integer> segmentDistances,
            List<Integer> segmentDurations,
            Integer totalDistanceM,
            Integer totalDurationMin
    ) {
        this.hubIds = List.copyOf(hubIds);
        this.segmentDistances = List.copyOf(segmentDistances);
        this.segmentDurations = List.copyOf(segmentDurations);
        this.totalDistanceM = totalDistanceM;
        this.totalDurationMin = totalDurationMin;
    }

    public static OptimalRoute of(
            List<UUID> hubIds,
            List<Integer> segmentDistances,
            List<Integer> segmentDurations,
            Integer totalDistanceM,
            Integer totalDurationMin
    ) {
        return new OptimalRoute(hubIds, segmentDistances, segmentDurations, totalDistanceM, totalDurationMin);
    }

    public int getHubCount() {
        return hubIds.size();
    }

    public boolean isEmpty() {
        return hubIds.isEmpty();
    }

    public Integer getSegmentDistance(int index) {
        return segmentDistances.get(index);
    }

    public Integer getSegmentDuration(int index) {
        return segmentDurations.get(index);
    }
}