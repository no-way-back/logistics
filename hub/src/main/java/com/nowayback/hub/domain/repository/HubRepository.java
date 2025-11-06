package com.nowayback.hub.domain.repository;

import com.nowayback.hub.domain.entity.HubEntity;

public interface HubRepository {
    boolean existsByName(String name);
    HubEntity save(HubEntity any);
}
