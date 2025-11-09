package com.nowayback.hub.application;

import com.nowayback.hub.application.command.CreateHubCommand;
import com.nowayback.hub.application.dto.HubResult;
import com.nowayback.hub.application.exception.HubApplicationErrorCode;
import com.nowayback.hub.application.exception.HubApplicationException;
import com.nowayback.hub.domain.entity.Hub;
import com.nowayback.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.nowayback.hub.application.exception.HubApplicationErrorCode.*;

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
            throw new HubApplicationException(HUB_NAME_ALREADY_EXISTS_EXCEPTION);
        }
    }

    private void validateDuplicateAddress(String address) {
        if (hubRepository.existsByAddress(address)) {
            throw new HubApplicationException(HUB_ADDRESS_ALEADY_EXISTS_EXCEPTION);
        }
    }
}
