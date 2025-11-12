package com.nowayback.delivery.application.service;

import java.util.UUID;

public interface UserClient {
    UserInfo getUserInfoById(UUID userId);

    record UserInfo(
            String slackId
    ) {}
}
