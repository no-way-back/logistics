package com.nowayback.hub.domain.repository;

import com.nowayback.hub.domain.entity.HubEntity;

public interface HubRepository {
    boolean existsByName(String name);
    boolean existsByAddress(String name);
    HubEntity save(HubEntity any);
}
