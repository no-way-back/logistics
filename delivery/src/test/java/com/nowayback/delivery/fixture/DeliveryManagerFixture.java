package com.nowayback.delivery.fixture;

import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliverySequence;
import com.nowayback.delivery.domain.deliverymanager.vo.HubId;

import java.util.UUID;

public class DeliveryManagerFixture {

    public static final UUID DELIVERY_MANAGER_UUID = UUID.randomUUID();

    public static final UUID HUB_UUID = UUID.randomUUID();
    public static final DeliveryManagerType MANAGER_TYPE = DeliveryManagerType.HUB;
    public static final String SLACK_ID = "slack_1234";
    public static final int SEQUENCE = 1;

    public static final HubId HUB_ID = HubId.of(HUB_UUID);
    public static final DeliverySequence DELIVERY_SEQUENCE = DeliverySequence.of(SEQUENCE);


}
