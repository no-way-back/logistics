package com.nowayback.hub.application.command;

import com.nowayback.hub.presentation.request.CreateHubRequest;
import jakarta.validation.Valid;

import java.math.BigDecimal;

public record CreateHubCommand (String name, String address, BigDecimal latitude, BigDecimal longitude) {
    public static CreateHubCommand of(CreateHubRequest request) {
        return new CreateHubCommand(
                request.name(),
                request.address(),
                request.latitude(),
                request.longitude()
        );
    }
}
