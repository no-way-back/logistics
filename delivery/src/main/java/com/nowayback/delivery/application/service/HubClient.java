package com.nowayback.delivery.application.service;

import java.util.UUID;

public interface HubClient {
    boolean existsById(UUID hubId);
}
