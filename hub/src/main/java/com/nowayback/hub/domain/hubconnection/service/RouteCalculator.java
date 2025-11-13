package com.nowayback.hub.domain.hubconnection.service;

import com.nowayback.hub.domain.hubconnection.entity.HubConnection;
import com.nowayback.hub.domain.hubconnection.vo.OptimalRoute;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RouteCalculator {

    /**
     * Dijkstra 알고리즘을 사용하여 최적 경로 계산
     * HubConnection(직선 거리)들을 조합하여 최적 경로를 찾습니다.
     *
     * @param startHubId 출발 허브 ID
     * @param endHubId 도착 허브 ID
     * @param connections 모든 허브 직선 연결 정보
     * @return 최적 경로 (거리 기준)
     */
    public OptimalRoute calculate(
            UUID startHubId,
            UUID endHubId,
            List<HubConnection> connections
    ) {
        Map<UUID, List<Edge>> graph = buildGraph(connections);

        DijkstraResult result = dijkstra(graph, startHubId, endHubId);

        // 경로를 찾지 못했을 경우
        if (result.distance == Integer.MAX_VALUE) {
            return OptimalRoute.of(
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    0,
                    0
            );
        }

        RouteWithSegments routeWithSegments = reconstructPath(
                graph,
                result.previous,
                startHubId,
                endHubId
        );

        return OptimalRoute.of(
                routeWithSegments.path,
                routeWithSegments.segmentDistances,
                routeWithSegments.segmentDurations,
                result.distance,
                result.duration
        );
    }

    /**
     * 허브 연결 정보로부터 그래프 구성
     */
    private Map<UUID, List<Edge>> buildGraph(List<HubConnection> connections) {
        Map<UUID, List<Edge>> graph = new HashMap<>();

        for (HubConnection connection : connections) {
            UUID origin = connection.getConnectedHubs().getOriginHubId();
            UUID destination = connection.getConnectedHubs().getDestinationHubId();
            Integer distance = connection.getConnectionInfo().getDistanceM();
            Integer duration = connection.getConnectionInfo().getEstimatedDurationMin();

            // 출발지 허브 ID가 그래프에 없으면 새로 추가하고, 해당 허브에서 도착지로 가는 Edge를 인접 리스트에 추가
            graph.computeIfAbsent(origin, hubId -> new ArrayList<>())
                    .add(new Edge(destination, distance, duration));

            // 도착지 허브 ID가 그래프에 없으면 새로 추가하고, 해당 허브에서 출발지로 돌아오는 Edge를 추가하여 양방향 그래프를 완성
            graph.computeIfAbsent(destination, hubId -> new ArrayList<>())
                    .add(new Edge(origin, distance, duration));
        }

        return graph;
    }

    /**
     * 최단 경로 탐색
     */
    private DijkstraResult dijkstra(
            Map<UUID, List<Edge>> graph,
            UUID start,
            UUID end
    ) {
        Map<UUID, Integer> distances = new HashMap<>();
        Map<UUID, Integer> durations = new HashMap<>();
        Map<UUID, UUID> previous = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(
                Comparator.comparingInt(node -> node.distance)
        );

        distances.put(start, 0);
        durations.put(start, 0);
        pq.offer(new Node(start, 0, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            // 목적지에 도달한 경우
            if (current.hubId.equals(end)) {
                return new DijkstraResult(
                        distances.get(end),
                        durations.get(end),
                        previous
                );
            }

            // 이미 처리된 노드는 스킵
            if (current.distance > distances.getOrDefault(current.hubId, Integer.MAX_VALUE)) {
                continue;
            }

            // 인접 노드 탐색
            List<Edge> edges = graph.getOrDefault(current.hubId, Collections.emptyList());
            for (Edge edge : edges) {
                int newDistance = current.distance + edge.distance;
                int newDuration = current.duration + edge.duration;

                // 더 짧은 경로를 찾은 경우
                if (newDistance < distances.getOrDefault(edge.to, Integer.MAX_VALUE)) {
                    distances.put(edge.to, newDistance);
                    durations.put(edge.to, newDuration);
                    previous.put(edge.to, current.hubId);
                    pq.offer(new Node(edge.to, newDistance, newDuration));
                }
            }
        }

        // 경로를 찾지 못한 경우
        return new DijkstraResult(Integer.MAX_VALUE, Integer.MAX_VALUE, previous);
    }

    /**
     * 경로 재구성 및 세그먼트 정보 추출
     */
    private RouteWithSegments reconstructPath(
            Map<UUID, List<Edge>> graph,
            Map<UUID, UUID> previous,
            UUID start,
            UUID end
    ) {
        List<UUID> path = new ArrayList<>();
        List<Integer> segmentDistances = new ArrayList<>();
        List<Integer> segmentDurations = new ArrayList<>();

        UUID current = end;

        while (current != null) {
            path.add(current);
            if (current.equals(start)) {
                break;
            }
            current = previous.get(current);
        }

        Collections.reverse(path);

        for (int i = 0; i < path.size() - 1; i++) {
            UUID from = path.get(i);
            UUID to = path.get(i + 1);

            List<Edge> edges = graph.get(from);
            for (Edge edge : edges) {
                if (edge.to.equals(to)) {
                    segmentDistances.add(edge.distance);
                    segmentDurations.add(edge.duration);
                    break;
                }
            }
        }

        return new RouteWithSegments(path, segmentDistances, segmentDurations);
    }

    /**
     * 경로와 세그먼트 정보를 담는 내부 클래스
     */
    @AllArgsConstructor
    private static class RouteWithSegments {
        List<UUID> path;
        List<Integer> segmentDistances;
        List<Integer> segmentDurations;
    }

    /**
     * 그래프의 간선 정보
     */
    @AllArgsConstructor
    private static class Edge {
        UUID to;
        int distance;
        int duration;
    }

    /**
     * 큐에서 사용할 노드
     */
    @AllArgsConstructor
    private static class Node {
        UUID hubId;
        int distance;
        int duration;
    }

    /**
     * 최단 경로 탐색 결과
     */
    @AllArgsConstructor
    private static class DijkstraResult {
        int distance;
        int duration;
        Map<UUID, UUID> previous;
    }
}