package com.nowayback.hub.presentation;

import com.nowayback.hub.application.HubService;
import com.nowayback.hub.application.command.CreateHubCommand;
import com.nowayback.hub.application.command.UpdateHubCommand;
import com.nowayback.hub.presentation.request.CreateHubRequest;
import com.nowayback.hub.presentation.request.UpdateHubRequest;
import com.nowayback.hub.presentation.response.CreateHubResponse;
import com.nowayback.hub.presentation.response.UpdateHubResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/hubs")
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    @PostMapping
    public ResponseEntity<CreateHubResponse> create(@Valid @RequestBody CreateHubRequest request) {
        CreateHubCommand createHubCommand = CreateHubCommand.from(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CreateHubResponse.from(
                        hubService.create(createHubCommand))
                );
    }

    @PutMapping("/{hubId}")
    public ResponseEntity<UpdateHubResponse> update(@PathVariable UUID hubId, @RequestBody UpdateHubRequest request) {
        UpdateHubCommand updateHubCommand = UpdateHubCommand.from(request);

        return ResponseEntity.ok(
                UpdateHubResponse.from(
                        hubService.update(hubId, updateHubCommand))
        );
    }
}
