package com.nowayback.hub.application.hub;

import com.nowayback.hub.application.hub.dto.GetHubResult;
import com.nowayback.hub.application.hub.command.CreateHubCommand;
import com.nowayback.hub.application.hub.command.UpdateHubCommand;
import com.nowayback.hub.application.hub.dto.CreateHubResult;
import com.nowayback.hub.application.hub.dto.UpdateHubResult;
import com.nowayback.hub.application.hub.exception.HubApplicationException;
import com.nowayback.hub.domain.hub.entity.Hub;
import com.nowayback.hub.domain.hub.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.nowayback.hub.application.hub.exception.HubApplicationErrorCode.*;

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
        Hub hub = findHubOrThrow(hubId);

        if (command.name() != null && !command.name().equals(hub.getName())) {
            validateDuplicateName(command.name());
        }

        if (command.address() != null && !command.address().equals(hub.getAddress())) {
            validateDuplicateAddress(command.address());
        }

        hub.update(command);

        return UpdateHubResult.of(hub);
    }

    /**
     * 허브 단건 조회
     */
    @Transactional(readOnly = true)
    public GetHubResult getHub(UUID hubId) {
        Hub hub = findHubOrThrow(hubId);
        return GetHubResult.of(hub);
    }

    /**
     * 허브 전체 조회
     */
    @Transactional(readOnly = true)
    public Page<GetHubResult> getHubList(Pageable pageable) {
        Page<Hub> hubs = hubRepository.findAll(pageable);
        return GetHubResult.of(hubs);
    }

    /**
     * 허브 삭제
     */
    public void delete(UUID hubId, UUID userId) {
        Hub hub = findHubOrThrow(hubId);
        hub.delete(userId);
    }

    /**
     * 여러 개의 허브 조회
     */
    public List<Hub> getHubsByIds(Iterable<UUID> hubIds) {
        return hubRepository.findAllById(hubIds);
    }

    public Hub findHubOrThrow(UUID hubId) {
        return hubRepository.findById(hubId).orElseThrow(
                () -> new HubApplicationException(HUB_NOT_FOUND_EXCEPTION)
        );
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