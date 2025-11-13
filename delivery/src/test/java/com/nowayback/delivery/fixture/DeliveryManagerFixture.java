package com.nowayback.delivery.fixture;

import com.nowayback.delivery.application.deliverymanager.command.CreateDeliveryManagerCommand;
import com.nowayback.delivery.application.deliverymanager.dto.DeliveryManagerResult;
import com.nowayback.delivery.application.deliverymanager.service.UserClient;
import com.nowayback.delivery.domain.deliverymanager.entity.DeliveryManager;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliverySequence;
import com.nowayback.delivery.domain.deliverymanager.vo.HubId;
import com.nowayback.delivery.presentation.deliverymanager.dto.request.CreateDeliveryManagerRequest;

import java.util.List;
import java.util.UUID;

public class DeliveryManagerFixture {

    public static final UUID DELIVERY_MANAGER_UUID = UUID.randomUUID();

    public static final UUID HUB_UUID = UUID.randomUUID();
    public static final DeliveryManagerType MANAGER_TYPE = DeliveryManagerType.HUB;
    public static final String SLACK_ID = "slack_1234";
    public static final int SEQUENCE = 1;

    public static final HubId HUB_ID = HubId.of(HUB_UUID);
    public static final DeliverySequence DELIVERY_SEQUENCE = DeliverySequence.of(SEQUENCE);

    /* delivery manager entity */
    public static DeliveryManager createDeliveryManager() {
        return DeliveryManager.create(
                DELIVERY_MANAGER_UUID,
                HUB_ID,
                MANAGER_TYPE,
                SLACK_ID,
                DELIVERY_SEQUENCE
        );
    }

    public static DeliveryManager createDeliveryManager(UUID deliveryManagerId, DeliveryManagerType type, DeliverySequence sequence) {
        return DeliveryManager.create(
                deliveryManagerId,
                HUB_ID,
                type,
                SLACK_ID,
                sequence
        );
    }

    public static final List<DeliveryManager> DELIVERY_MANAGER_LIST_BY_TYPE = List.of(
            createDeliveryManager(UUID.randomUUID(), MANAGER_TYPE, DeliverySequence.of(1)),
            createDeliveryManager(UUID.randomUUID(), MANAGER_TYPE, DeliverySequence.of(2)),
            createDeliveryManager(UUID.randomUUID(), MANAGER_TYPE, DeliverySequence.of(3))
    );

    /* delivery manager command */
    public static final CreateDeliveryManagerCommand CREATE_DELIVERY_MANAGER_COMMAND = CreateDeliveryManagerCommand.of(
            DELIVERY_MANAGER_UUID,
            HUB_UUID,
            MANAGER_TYPE
    );

    /* user info */
    public static final UserClient.UserInfo USER_INFO = new UserClient.UserInfo(
            SLACK_ID
    );

    /* delivery manager result */
    public static final DeliveryManagerResult DELIVERY_MANAGER_RESULT = DeliveryManagerResult.from(createDeliveryManager());

    /* delivery manager request dto */
    public static final CreateDeliveryManagerRequest CREATE_DELIVERY_MANAGER_REQUEST = new CreateDeliveryManagerRequest(
            DELIVERY_MANAGER_UUID,
            HUB_UUID,
            MANAGER_TYPE
    );

    public static final CreateDeliveryManagerRequest INVALID_CREATE_DELIVERY_MANAGER_REQUEST = new CreateDeliveryManagerRequest(
            null,
            HUB_UUID,
            MANAGER_TYPE
    );
}
