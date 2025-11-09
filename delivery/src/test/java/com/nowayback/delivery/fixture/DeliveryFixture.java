package com.nowayback.delivery.fixture;

import com.nowayback.delivery.application.command.CreateDeliveryCommand;
import com.nowayback.delivery.application.command.UpdateDeliveryRecipientInfoCommand;
import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.vo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class DeliveryFixture {

    public static final UUID DELIVERY_UUID = UUID.randomUUID();

    public static final UUID ORDER_UUID = UUID.randomUUID();
    private static final UUID HUB_UUID = UUID.randomUUID();
    public static final UUID SOURCE_HUB_UUID = UUID.randomUUID();
    public static final UUID DESTINATION_HUB_UUID = UUID.randomUUID();
    public static final UUID COMPANY_DELIVERY_MANAGER_UUID = UUID.randomUUID();

    public static final OrderId ORDER_ID = OrderId.of(ORDER_UUID);
    public static final HubId HUB_ID = HubId.of(HUB_UUID);
    public static final HubId SOURCE_HUB_ID = HubId.of(SOURCE_HUB_UUID);
    public static final HubId DESTINATION_HUB_ID = HubId.of(DESTINATION_HUB_UUID);

    public static final String DELIVERY_ADDRESS = "서울특별시 중구 다산로46길 17 119호";
    public static final String RECIPIENT_NAME = "홍길동";
    public static final String RECIPIENT_SLACK_ID = "slack_1234";
    public static final RecipientInfo RECIPIENT_INFO = RecipientInfo.of(DELIVERY_ADDRESS, RECIPIENT_NAME, RECIPIENT_SLACK_ID);

    public static final String MODIFIED_DELIVERY_NAME = "김철수";
    public static final String MODIFIED_DELIVERY_SLACK_ID = "slack_5678";
    public static final RecipientInfo MODIFIED_RECIPIENT_INFO = RecipientInfo.of(DELIVERY_ADDRESS, MODIFIED_DELIVERY_NAME, MODIFIED_DELIVERY_SLACK_ID);

    public static final DeliveryManagerId COMPANY_DELIVERY_MANAGER_ID = DeliveryManagerId.of(COMPANY_DELIVERY_MANAGER_UUID);

    public static final DeliveryStatus DELIVERY_STATUS = DeliveryStatus.WAITING_AT_HUB;

    public static final int PAGE = 0;
    public static final int SIZE = 10;

    /* delivery entity */
    public static Delivery createDelivery() {
        return Delivery.create(
                ORDER_ID,
                SOURCE_HUB_ID,
                DESTINATION_HUB_ID,
                RECIPIENT_INFO,
                COMPANY_DELIVERY_MANAGER_ID
        );
    }

    public static Delivery createDeliveryWithStatus(DeliveryStatus status) {
        Delivery delivery = createDelivery();
        setPrivateField(delivery, "status", status);
        return delivery;
    }

    public static final Page<Delivery> DELIVERY_PAGE = new PageImpl<>(
            List.of(createDelivery(), createDelivery(), createDelivery(), createDelivery()),
            PageRequest.of(PAGE, SIZE),
            2
    );

    /* delivery command */
    public static final CreateDeliveryCommand CREATE_DELIVERY_COMMAND = new CreateDeliveryCommand(
            ORDER_UUID,
            SOURCE_HUB_UUID,
            DESTINATION_HUB_UUID,
            DELIVERY_ADDRESS,
            RECIPIENT_NAME,
            RECIPIENT_SLACK_ID
    );

    public static final UpdateDeliveryRecipientInfoCommand UPDATE_DELIVERY_RECIPIENT_INFO_COMMAND = new UpdateDeliveryRecipientInfoCommand(
            DELIVERY_ADDRESS,
            MODIFIED_DELIVERY_NAME,
            MODIFIED_DELIVERY_SLACK_ID
    );

    private static void setPrivateField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
