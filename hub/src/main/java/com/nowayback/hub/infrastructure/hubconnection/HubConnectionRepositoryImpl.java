package com.nowayback.hub.infrastructure.hubconnection;

import com.nowayback.hub.domain.hubconnection.entity.HubConnection;
import com.nowayback.hub.domain.hubconnection.repository.HubConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HubConnectionRepositoryImpl implements HubConnectionRepository {

    private final HubConnectionJPARepository jpaRepository;

    @Override
    public List<HubConnection> findAll() {
        return jpaRepository.findAll();
    }
}
