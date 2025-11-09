package com.nowayback.hub.domain.repository;

import com.nowayback.hub.domain.entity.Hub;

public interface HubRepository {
    boolean existsByName(String name);
    boolean existsByAddress(String name);
    Hub save(Hub hub);
}
