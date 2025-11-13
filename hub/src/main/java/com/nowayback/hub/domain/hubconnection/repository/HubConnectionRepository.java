package com.nowayback.hub.domain.hubconnection.repository;

import com.nowayback.hub.domain.hubconnection.entity.HubConnection;

import java.util.List;

public interface HubConnectionRepository {
    List<HubConnection> findAll();
}
