package com.nowayback.hub.presentation.hub;

import com.nowayback.common.security.annotation.AuthUser;
import com.nowayback.common.security.annotation.CurrentUser;
import com.nowayback.common.security.annotation.RequireRole;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.hub.application.hub.HubService;
import com.nowayback.hub.application.hub.command.CreateHubCommand;
import com.nowayback.hub.application.hub.command.UpdateHubCommand;
import com.nowayback.hub.presentation.hub.request.CreateHubRequest;
import com.nowayback.hub.presentation.hub.request.UpdateHubRequest;
import com.nowayback.hub.presentation.hub.response.CreateHubResponse;
import com.nowayback.hub.presentation.hub.response.UpdateHubResponse;
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
    @RequireRole(UserRole.MASTER)
    public ResponseEntity<CreateHubResponse> create(
            @Valid @RequestBody CreateHubRequest request) {
        CreateHubCommand createHubCommand = CreateHubCommand.from(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CreateHubResponse.from(
                        hubService.create(createHubCommand))
                );
    }

    @PatchMapping("/{hubId}")
    @RequireRole(UserRole.MASTER)
    public ResponseEntity<UpdateHubResponse> update(@PathVariable UUID hubId, @RequestBody UpdateHubRequest request) {
        UpdateHubCommand updateHubCommand = UpdateHubCommand.from(request);

        return ResponseEntity.ok(
                UpdateHubResponse.from(
                        hubService.update(hubId, updateHubCommand))
        );
    }

    @DeleteMapping("/{hubId}")
    @RequireRole(UserRole.MASTER)
    public ResponseEntity<Void> delete(@PathVariable UUID hubId, @CurrentUser AuthUser user) {
        hubService.delete(hubId, user.userId());
        return ResponseEntity.noContent().build();
    }
}