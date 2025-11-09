package com.nowayback.hub.infrastructure;

import com.nowayback.hub.domain.entity.Hub;
import com.nowayback.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository {

    private final HubJpaRepository jpaRepository;

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
