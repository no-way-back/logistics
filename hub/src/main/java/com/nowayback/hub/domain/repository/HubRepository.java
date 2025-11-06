package com.nowayback.hub.domain.repository;

import com.nowayback.hub.domain.entity.HubEntity;

import java.util.Optional;

public interface HubRepository {
    boolean existsByName(String name);
    HubEntity save(HubEntity any);
}
