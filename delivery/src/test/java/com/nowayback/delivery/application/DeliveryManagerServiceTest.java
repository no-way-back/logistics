package com.nowayback.delivery.application;

import com.nowayback.delivery.application.command.CreateDeliveryManagerCommand;
import com.nowayback.delivery.application.dto.DeliveryManagerResult;
import com.nowayback.delivery.application.exception.DeliveryManagerApplicationErrorCode;
import com.nowayback.delivery.application.exception.DeliveryManagerApplicationException;
import com.nowayback.delivery.application.service.UserClient;
import com.nowayback.delivery.domain.deliverymanager.entity.DeliveryManager;
import com.nowayback.delivery.domain.deliverymanager.repository.DeliveryManagerRepository;
import com.nowayback.delivery.domain.deliverymanager.vo.DeliveryManagerType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.nowayback.delivery.fixture.DeliveryManagerFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("배송 담당자 서비스")
class DeliveryManagerServiceTest {

    @Mock
    private DeliveryManagerRepository deliveryManagerRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private DeliveryManagerService deliveryManagerService;

    @Nested
    @DisplayName("배송 담당자 생성")
    class CreateDeliveryManager {

        @Test
        @DisplayName("유효한 정보로 배송 담당자를 생성하면 순번이 가장 큰 값 + 1로 생성된다.")
        void createDeliveryManager_Success() {
            /* given */
            CreateDeliveryManagerCommand command = CREATE_DELIVERY_MANAGER_COMMAND;
            int sequenceBeforeCreate = 2;

            when(deliveryManagerRepository.existsById(any())).thenReturn(false);
            when(deliveryManagerRepository.findMaxSequenceByType(command.type())).thenReturn(sequenceBeforeCreate);
            when(userClient.getUserInfoById(command.userId())).thenReturn(USER_INFO);
            when(deliveryManagerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            /* when */
            DeliveryManagerResult result = deliveryManagerService.createDeliveryManager(command);

            /* then */
            assertThat(result.sequence()).isEqualTo(sequenceBeforeCreate + 1);
            verify(deliveryManagerRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("이미 존재하는 배송 담당자 ID로 생성하려 하면 예외가 발생한다.")
        void createDeliveryManager_DuplicateId_Exception() {
            /* given */
            CreateDeliveryManagerCommand command = CREATE_DELIVERY_MANAGER_COMMAND;

            when(deliveryManagerRepository.existsById(command.userId())).thenReturn(true);

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryManagerService.createDeliveryManager(command))
                    .isInstanceOf(DeliveryManagerApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryManagerApplicationErrorCode.DUPLICATE_DELIVERY_MANAGER_ID);
        }
    }

    @Nested
    @DisplayName("배송 담당자 단일 조회")
    class GetDeliveryManager {

        @Test
        @DisplayName("존재하는 배송 담당자 ID로 조회하면 배송 담당자 정보를 반환한다.")
        void getDeliveryManager_Success() {
            /* given */
            UUID deliveryManagerId = DELIVERY_MANAGER_UUID;
            DeliveryManager deliveryManager = createDeliveryManager();

            when(deliveryManagerRepository.findById(deliveryManagerId)).thenReturn(Optional.of(deliveryManager));

            /* when */
            DeliveryManagerResult result = deliveryManagerService.getDeliveryManager(deliveryManagerId);

            /* then */
            assertThat(result.deliveryManagerId()).isEqualTo(deliveryManager.getId());
            verify(deliveryManagerRepository, times(1)).findById(deliveryManagerId);
        }

        @Test
        @DisplayName("존재하지 않는 배송 담당자 ID로 조회하면 예외가 발생한다.")
        void getDeliveryManager_NotFound_Exception() {
            /* given */
            UUID deliveryManagerId = DELIVERY_MANAGER_UUID;

            when(deliveryManagerRepository.findById(deliveryManagerId)).thenReturn(Optional.empty());

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryManagerService.getDeliveryManager(deliveryManagerId))
                    .isInstanceOf(DeliveryManagerApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryManagerApplicationErrorCode.NOT_FOUND_DELIVERY_MANAGER);
        }
    }

    @Nested
    @DisplayName("배송 담당자 할당")
    class AssignDeliveryManager {

        @Test
        @DisplayName("배송 담당자 유형에 맞는 담당자가 존재하면 라운드로빈 방식으로 배송 담당자를 할당한다.")
        void assignDeliveryManager_Success() {
            /* given */
            DeliveryManagerType type = MANAGER_TYPE;

            when(deliveryManagerRepository.findAllByTypeOrderBySequenceAsc(type))
                    .thenReturn(DELIVERY_MANAGER_LIST_BY_TYPE);

            /* when */
            UUID assignedManagerId1 = deliveryManagerService.assignDeliveryManager(type);
            UUID assignedManagerId2 = deliveryManagerService.assignDeliveryManager(type);
            UUID assignedManagerId3 = deliveryManagerService.assignDeliveryManager(type);
            UUID assignedManagerId4 = deliveryManagerService.assignDeliveryManager(type);

            /* then */
            assertThat(assignedManagerId1).isEqualTo(DELIVERY_MANAGER_LIST_BY_TYPE.get(0).getId());
            assertThat(assignedManagerId2).isEqualTo(DELIVERY_MANAGER_LIST_BY_TYPE.get(1).getId());
            assertThat(assignedManagerId3).isEqualTo(DELIVERY_MANAGER_LIST_BY_TYPE.get(2).getId());
            assertThat(assignedManagerId4).isEqualTo(DELIVERY_MANAGER_LIST_BY_TYPE.get(0).getId());
        }

        @Test
        @DisplayName("배송 담당자 유형에 맞는 담당자가 없으면 예외가 발생한다.")
        void assignDeliveryManager_NotFound_Exception() {
            /* given */
            DeliveryManagerType type = MANAGER_TYPE;

            when(deliveryManagerRepository.findAllByTypeOrderBySequenceAsc(type))
                    .thenReturn(Collections.emptyList());

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryManagerService.assignDeliveryManager(type))
                    .isInstanceOf(DeliveryManagerApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryManagerApplicationErrorCode.NOT_FOUND_DELIVERY_MANAGER);
        }
    }

    @Nested
    @DisplayName("배송 담당자 삭제")
    class DeleteDeliveryManager {

        @Test
        @DisplayName("유효한 ID로 배송 담당자를 삭제하면 배송 담당자가 삭제된다.")
        void deleteDeliveryManager_Success() {
            /* given */
            UUID actorId = UUID.randomUUID();
            UUID deliveryManagerId = DELIVERY_MANAGER_UUID;

            DeliveryManager deliveryManager = createDeliveryManager();

            when(deliveryManagerRepository.findById(deliveryManagerId)).thenReturn(Optional.of(deliveryManager));

            /* when */
            deliveryManagerService.deleteDeliveryManager(actorId, deliveryManagerId);

            /* then */
            assertThat(deliveryManager.getDeletedAt()).isNotNull();
            verify(deliveryManagerRepository, times(1)).findById(deliveryManagerId);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 배송 담당자를 삭제하려 하면 예외가 발생한다.")
        void deleteDeliveryManager_NotFound_Exception() {
            /* given */
            UUID actorId = UUID.randomUUID();
            UUID deliveryManagerId = DELIVERY_MANAGER_UUID;

            when(deliveryManagerRepository.findById(deliveryManagerId)).thenReturn(Optional.empty());

            /* when */
            /* then */
            assertThatThrownBy(() -> deliveryManagerService.deleteDeliveryManager(actorId, deliveryManagerId))
                    .isInstanceOf(DeliveryManagerApplicationException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DeliveryManagerApplicationErrorCode.NOT_FOUND_DELIVERY_MANAGER);
        }
    }
}