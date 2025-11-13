package com.nowayback.hub.presentation.hubconnection;

import com.nowayback.hub.application.hubconnection.HubConnectionService;
import com.nowayback.hub.application.hubconnection.dto.OptimalRouteResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/hub-connections")
@RequiredArgsConstructor
public class HubConnectionController {

    private final HubConnectionService hubConnectionService;

    /**
     * 두 허브 간 최적 경로 조회
     *
     * @param startHubId 출발 허브 ID
     * @param endHubId 도착 허브 ID
     * @return 최적 경로 정보
     */
    @GetMapping("/optimal-route")
    public ResponseEntity<OptimalRouteResult> findOptimalRoute(
            @RequestParam UUID startHubId,
            @RequestParam UUID endHubId
    ) {
        return ResponseEntity.ok(
                hubConnectionService.findOptimalRoute(startHubId, endHubId)
        );
    }
}