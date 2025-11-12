package com.nowayback.hub.domain.hub.repository;

import com.nowayback.hub.domain.hub.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface HubRepository {
    boolean existsByName(String name);
    boolean existsByAddress(String name);
    Hub save(Hub hub);
    Optional<Hub> findById(UUID hubId);

    Page<Hub> findAll(Pageable pageable);
}