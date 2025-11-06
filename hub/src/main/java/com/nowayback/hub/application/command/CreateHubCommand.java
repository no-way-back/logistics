package com.nowayback.hub.application.command;

import java.math.BigDecimal;

public record CreateHubCommand (String name, String address, BigDecimal latitude, BigDecimal longitude) {

}
