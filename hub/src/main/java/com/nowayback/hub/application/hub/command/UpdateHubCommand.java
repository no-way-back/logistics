package com.nowayback.hub.application.hub.command;

import com.nowayback.hub.presentation.hub.request.UpdateHubRequest;

import java.math.BigDecimal;

public record UpdateHubCommand(
        String name,
        String address,
        BigDecimal latitude,
        BigDecimal longitude
) {
    public static UpdateHubCommand from(UpdateHubRequest request) {
        return new UpdateHubCommand(
                request.name(),
                request.address(),
                request.latitude(),
                request.longitude()
        );
    }
}