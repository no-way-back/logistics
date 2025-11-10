package com.nowayback.hub.infrastructure;

import com.nowayback.hub.domain.entity.Hub;
import com.nowayback.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository {

    private final HubJpaRepository jpaRepository;

    @Override
    public Optional<Hub> findById(UUID hubId) {
        return jpaRepository.findById(hubId);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsByAddress(String address) {
        return jpaRepository.existsByAddress(address);
    }

    @Override
    public Hub save(Hub hub) {
        return jpaRepository.save(hub);
    }
}
