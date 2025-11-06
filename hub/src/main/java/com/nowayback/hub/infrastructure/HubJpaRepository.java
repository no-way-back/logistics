package com.nowayback.hub.infrastructure;

import com.nowayback.hub.domain.entity.HubEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubJpaRepository extends JpaRepository<HubEntity, UUID> {
    boolean existsByName(String name);

    boolean existsByAddress(String address);
}
