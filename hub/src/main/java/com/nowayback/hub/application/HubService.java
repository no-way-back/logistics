package com.nowayback.hub.application;

import com.nowayback.hub.application.command.CreateHubCommand;
import com.nowayback.hub.application.command.UpdateHubCommand;
import com.nowayback.hub.application.dto.CreateHubResult;
import com.nowayback.hub.application.dto.UpdateHubResult;
import com.nowayback.hub.application.exception.HubApplicationException;
import com.nowayback.hub.domain.entity.Hub;
import com.nowayback.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.nowayback.hub.application.exception.HubApplicationErrorCode.*;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;

    /**
     * 허브 생성
     */
    @Transactional
    public CreateHubResult create(CreateHubCommand command) {
        validateDuplicateNameAndAddress(command.name(), command.address());
        Hub hub = Hub.create(command);

        return CreateHubResult.of(hubRepository.save(hub));
    }

    /**
     * 허브 수정
     */
    @Transactional
    public UpdateHubResult update(UUID hubId, UpdateHubCommand command) {
        Hub hub = hubRepository.findById(hubId).orElseThrow(
                () -> new HubApplicationException(HUB_NOT_FOUND_EXCEPTION)
        );

        if (command.name() != null && !command.name().equals(hub.getName())) {
            validateDuplicateName(command.name());
        }

        if (command.address() != null && !command.address().equals(hub.getAddress())) {
            validateDuplicateAddress(command.address());
        }

        hub.update(command);

        return UpdateHubResult.of(hub);
    }

    private void validateDuplicateNameAndAddress(String name, String address) {
        validateDuplicateName(name);
        validateDuplicateAddress(address);
    }

    private void validateDuplicateName(String name) {
        if (hubRepository.existsByName(name)) {
            throw new HubApplicationException(HUB_NAME_ALREADY_EXISTS_EXCEPTION);
        }
    }

    private void validateDuplicateAddress(String address) {
        if (hubRepository.existsByAddress(address)) {
            throw new HubApplicationException(HUB_ADDRESS_ALREADY_EXISTS_EXCEPTION);
        }
    }
}
