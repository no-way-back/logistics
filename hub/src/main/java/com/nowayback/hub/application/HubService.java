package com.nowayback.hub.application;

import com.nowayback.hub.application.command.CreateHubCommand;
import com.nowayback.hub.application.dto.HubResult;
import com.nowayback.hub.domain.entity.Hub;
import com.nowayback.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;

    public HubResult create(CreateHubCommand command) {
        validateDuplicateName(command.name());
        validateDuplicateAddress(command.address());

        Hub hub = Hub.create(command);

        return HubResult.from(hubRepository.save(hub));
    }

    private void validateDuplicateName(String name) {
        if (hubRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 허브 이름입니다.");
        }
    }

    private void validateDuplicateAddress(String address) {
        if (hubRepository.existsByAddress(address)) {
            throw new IllegalArgumentException("이미 존재하는 허브 주소입니다.");
        }
    }
}
