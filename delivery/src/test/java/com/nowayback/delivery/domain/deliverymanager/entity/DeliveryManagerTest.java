package com.nowayback.delivery.domain.deliverymanager.entity;

import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliverySequence;
import com.nowayback.delivery.domain.deliverymanager.vo.HubId;
import com.nowayback.delivery.domain.deliverymanager.exception.DeliveryManagerDomainErrorCode;
import com.nowayback.delivery.domain.deliverymanager.exception.DeliveryManagerDomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.nowayback.delivery.fixture.DeliveryManagerFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("배송 담당자 엔티티")
class DeliveryManagerTest {

    @Nested
    @DisplayName("배송 담당자 생성")
    class CreateDeliveryManager {

        @Test
        @DisplayName("모든 필드가 정상일 경우 배송 담당자 생성에 성공한다.")
        void createDeliveryManager_success () {
            /* given */
            UUID deliveryManagerId = DELIVERY_MANAGER_UUID;
            HubId hubId = HUB_ID;
            DeliveryManagerType deliveryManagerType = MANAGER_TYPE;
            String slackId = SLACK_ID;
            DeliverySequence deliverySequence = DELIVERY_SEQUENCE;

            /* when */
            DeliveryManager deliveryManager = DeliveryManager.create(
                    deliveryManagerId,
                    hubId,
                    deliveryManagerType,
                    slackId,
                    deliverySequence
            );

            /* then */
            assertThat(deliveryManager.getId()).isEqualTo(deliveryManagerId);
            assertThat(deliveryManager.getHubId()).isEqualTo(hubId);
            assertThat(deliveryManager.getType()).isEqualTo(deliveryManagerType);
            assertThat(deliveryManager.getSlackId()).isEqualTo(slackId);
            assertThat(deliveryManager.getDeliverySequence()).isEqualTo(deliverySequence);
        }

        @Test
        @DisplayName("배송 담당자 ID는 null일 수 없다.")
        void createDeliveryManager_WhenIdIsNull_ShouldThrowException() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> DeliveryManager.create(null, HUB_ID, MANAGER_TYPE, SLACK_ID, DELIVERY_SEQUENCE))
                    .isInstanceOf(DeliveryManagerDomainException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryManagerDomainErrorCode.NULL_USER_ID_VALUE);
        }

        @Test
        @DisplayName("허브 ID는 null일 수 없다.")
        void createDeliveryManager_WhenHubIdIsNull_ShouldThrowException() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> DeliveryManager.create(DELIVERY_MANAGER_UUID, null, MANAGER_TYPE, SLACK_ID, DELIVERY_SEQUENCE))
                    .isInstanceOf(DeliveryManagerDomainException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryManagerDomainErrorCode.NULL_HUB_ID_OBJECT);
        }

        @Test
        @DisplayName("배송 담당자 타입은 null일 수 없다.")
        void createDeliveryManager_WhenTypeIsNull_ShouldThrowException() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> DeliveryManager.create(DELIVERY_MANAGER_UUID, HUB_ID, null, SLACK_ID, DELIVERY_SEQUENCE))
                    .isInstanceOf(DeliveryManagerDomainException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryManagerDomainErrorCode.NULL_DELIVERY_MANAGER_TYPE_OBJECT);
        }

        @Test
        @DisplayName("슬랙 ID는 null일 수 없다.")
        void createDeliveryManager_WhenSlackIdIsNull_ShouldThrowException() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> DeliveryManager.create(DELIVERY_MANAGER_UUID, HUB_ID, MANAGER_TYPE, null, DELIVERY_SEQUENCE))
                    .isInstanceOf(DeliveryManagerDomainException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryManagerDomainErrorCode.NULL_SLACK_ID_VALUE);
        }

        @Test
        @DisplayName("배송 순서는 null일 수 없다.")
        void createDeliveryManager_WhenDeliverySequenceIsNull_ShouldThrowException() {
            /* given */
            /* when */
            /* then */
            assertThatThrownBy(() -> DeliveryManager.create(DELIVERY_MANAGER_UUID, HUB_ID, MANAGER_TYPE, SLACK_ID, null))
                    .isInstanceOf(DeliveryManagerDomainException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryManagerDomainErrorCode.NULL_DELIVERY_SEQUENCE_OBJECT);
        }
    }

    @Nested
    @DisplayName("배송 담당자 삭제")
    class DeleteDeliveryManager {

        @Test
        @DisplayName("삭제 시 소프트 삭제 처리된다.")
        void deleteDeliveryManager_success() {
            /* given */
            DeliveryManager deliveryManager = DeliveryManager.create(
                    DELIVERY_MANAGER_UUID,
                    HUB_ID,
                    MANAGER_TYPE,
                    SLACK_ID,
                    DELIVERY_SEQUENCE
            );
            UUID deletedBy = UUID.randomUUID();

            /* when */
            deliveryManager.delete(deletedBy);

            /* then */
            assertThat(deliveryManager.getDeletedBy()).isEqualTo(deletedBy);
            assertThat(deliveryManager.getDeletedAt()).isNotNull();
        }
    }
}