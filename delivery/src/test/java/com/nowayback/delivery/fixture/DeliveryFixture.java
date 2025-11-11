package com.nowayback.delivery.fixture;

import com.nowayback.delivery.application.command.CreateDeliveryCommand;
import com.nowayback.delivery.application.command.UpdateDeliveryRecipientInfoCommand;
import com.nowayback.delivery.application.command.UpdateDeliveryStatusCommand;
import com.nowayback.delivery.application.dto.DeliveryResult;
import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.vo.*;
import com.nowayback.delivery.presentation.dto.request.CreateDeliveryRequest;
import com.nowayback.delivery.presentation.dto.request.UpdateDeliveryRecipientInfoRequest;
import com.nowayback.delivery.presentation.dto.request.UpdateDeliveryStatusRequest;
import com.nowayback.delivery.presentation.dto.response.DeliveryResponse;
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

    public static Delivery createDelivery(OrderId orderId, HubId sourceHubId, HubId destinationHubId, DeliveryStatus status) {
        Delivery delivery = Delivery.create(
                orderId,
                sourceHubId,
                destinationHubId,
                RECIPIENT_INFO,
                COMPANY_DELIVERY_MANAGER_ID
        );
        setPrivateField(delivery, "status", status);
        return delivery;
    }

    public static Delivery createDeliveryWithStatus(DeliveryStatus status) {
        Delivery delivery = createDelivery();
        setPrivateField(delivery, "status", status);
        return delivery;
    }

    public static final Page<Delivery> DELIVERY_PAGE = new PageImpl<>(
            List.of(createDelivery(), createDelivery()),
            PageRequest.of(PAGE, SIZE),
            2
    );

    /* delivery command */
    public static final CreateDeliveryCommand CREATE_DELIVERY_COMMAND = CreateDeliveryCommand.of(
            ORDER_UUID,
            SOURCE_HUB_UUID,
            DESTINATION_HUB_UUID,
            DELIVERY_ADDRESS,
            RECIPIENT_NAME,
            RECIPIENT_SLACK_ID
    );

    public static final UpdateDeliveryRecipientInfoCommand UPDATE_DELIVERY_RECIPIENT_INFO_COMMAND = UpdateDeliveryRecipientInfoCommand.of(
            DELIVERY_ADDRESS,
            MODIFIED_DELIVERY_NAME,
            MODIFIED_DELIVERY_SLACK_ID
    );

    public static final UpdateDeliveryStatusCommand UPDATE_DELIVERY_STATUS_COMMAND = new UpdateDeliveryStatusCommand(
            DeliveryStatus.TRANSIT_BETWEEN_HUBS
    );

    /* delivery result */
    public static final DeliveryResult DELIVERY_RESULT = DeliveryResult.from(createDelivery());
    public static final DeliveryResult MODIFIED_DELIVERY_RESULT = DeliveryResult.from(Delivery.create(
            ORDER_ID,
            SOURCE_HUB_ID,
            DESTINATION_HUB_ID,
            MODIFIED_RECIPIENT_INFO,
            COMPANY_DELIVERY_MANAGER_ID
    ));
    public static final DeliveryResult DELIVERY_RESULT_TRANSIT_BETWEEN_HUBS = DeliveryResult.from(createDeliveryWithStatus(DeliveryStatus.TRANSIT_BETWEEN_HUBS));

    public static final Page<DeliveryResult> DELIVERY_RESULT_PAGE = DELIVERY_PAGE.map(DeliveryResult::from);

    /* delivery request dto */
    public static final CreateDeliveryRequest VALID_CREATE_DELIVERY_REQUEST = new CreateDeliveryRequest(
            ORDER_UUID,
            SOURCE_HUB_UUID,
            DESTINATION_HUB_UUID,
            DELIVERY_ADDRESS,
            RECIPIENT_NAME,
            RECIPIENT_SLACK_ID
    );

    public static final CreateDeliveryRequest INVALID_CREATE_DELIVERY_REQUEST = new CreateDeliveryRequest(
            null,
            SOURCE_HUB_UUID,
            DESTINATION_HUB_UUID,
            "",
            "",
            RECIPIENT_SLACK_ID
    );

    public static final UpdateDeliveryRecipientInfoRequest VALID_UPDATE_DELIVERY_RECIPIENT_INFO_REQUEST = new UpdateDeliveryRecipientInfoRequest(
            DELIVERY_ADDRESS,
            MODIFIED_DELIVERY_NAME,
            MODIFIED_DELIVERY_SLACK_ID
    );

    public static final UpdateDeliveryRecipientInfoRequest INVALID_UPDATE_DELIVERY_RECIPIENT_INFO_REQUEST = new UpdateDeliveryRecipientInfoRequest(
            DELIVERY_ADDRESS,
            "",
            ""
    );

    public static final UpdateDeliveryStatusRequest VALID_UPDATE_DELIVERY_STATUS_REQUEST = new UpdateDeliveryStatusRequest(
            DeliveryStatus.TRANSIT_BETWEEN_HUBS
    );

    public static final UpdateDeliveryStatusRequest INVALID_UPDATE_DELIVERY_STATUS_REQUEST = new UpdateDeliveryStatusRequest(
            null
    );

    /* delivery response dto */
    public static final DeliveryResponse DELIVERY_RESPONSE = DeliveryResponse.from(DELIVERY_RESULT);
    public static final DeliveryResponse MODIFIED_DELIVERY_RESPONSE = DeliveryResponse.from(MODIFIED_DELIVERY_RESULT);
    public static final DeliveryResponse DELIVERY_RESPONSE_TRANSIT_BETWEEN_HUBS = DeliveryResponse.from(DELIVERY_RESULT_TRANSIT_BETWEEN_HUBS);

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
