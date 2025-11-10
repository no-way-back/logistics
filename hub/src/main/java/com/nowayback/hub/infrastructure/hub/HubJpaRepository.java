package com.nowayback.hub.infrastructure.hub;

import com.nowayback.hub.domain.hub.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubJpaRepository extends JpaRepository<Hub, UUID> {
    boolean existsByName(String name);

    boolean existsByAddress(String address);
}
