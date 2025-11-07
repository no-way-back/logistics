package com.nowayback.hub.presentation;

import com.nowayback.hub.application.HubService;
import com.nowayback.hub.application.command.CreateHubCommand;
import com.nowayback.hub.presentation.request.CreateHubRequest;
import com.nowayback.hub.presentation.response.CreateHubResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hubs")
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    @PostMapping
    public ResponseEntity<CreateHubResponse> create(@Valid @RequestBody CreateHubRequest request) {
        CreateHubCommand createHubCommand = CreateHubCommand.of(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CreateHubResponse.of(
                        hubService.create(createHubCommand))
                );
    }
}
