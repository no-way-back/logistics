package com.nowayback.hub.application.hub.command;

import com.nowayback.hub.presentation.hub.request.CreateHubRequest;

import java.math.BigDecimal;

public record CreateHubCommand (String name, String address, BigDecimal latitude, BigDecimal longitude) {
    public static CreateHubCommand from(CreateHubRequest request) {
        return new CreateHubCommand(
                request.name(),
                request.address(),
                request.latitude(),
                request.longitude()
        );
    }
}