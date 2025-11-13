package com.nowayback.delivery.application.delivery.service;

import java.util.UUID;

public interface HubClient {
    boolean existsById(UUID hubId);
}
