package com.nowayback.hub.domain.hub.repository;

import com.nowayback.hub.domain.hub.entity.Hub;

import java.util.Optional;
import java.util.UUID;

public interface HubRepository {
    boolean existsByName(String name);
    boolean existsByAddress(String name);
    Hub save(Hub hub);
    Optional<Hub> findById(UUID hubId);
}