package com.nowayback.hub.infrastructure.hub;

import com.nowayback.hub.domain.hub.entity.Hub;
import com.nowayback.hub.domain.hub.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    public List<Hub> findAllById(Iterable<UUID> hubIds) {
        return jpaRepository.findAllById(hubIds);
    }

    @Override
    public Page<Hub> findByNameContaining(String name, Pageable pageable) {
        return jpaRepository.findByNameContaining(name, pageable);
    }

    @Override
    public Page<Hub> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
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
