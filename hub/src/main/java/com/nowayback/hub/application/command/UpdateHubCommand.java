package com.nowayback.hub.application.command;

import java.math.BigDecimal;

public record UpdateHubCommand(
        String name,
        String address,
        BigDecimal latitude,
        BigDecimal longitude
) {
    public static UpdateHubCommand of(String name, String address, BigDecimal latitude, BigDecimal longitude) {
        return new UpdateHubCommand(name, address, latitude, longitude);
    }
}