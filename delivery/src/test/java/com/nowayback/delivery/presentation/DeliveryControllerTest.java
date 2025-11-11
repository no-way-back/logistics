package com.nowayback.delivery.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.delivery.application.DeliveryService;
import com.nowayback.delivery.application.dto.DeliveryResult;
import com.nowayback.delivery.application.exception.DeliveryApplicationException;
import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;
import com.nowayback.delivery.presentation.dto.request.CreateDeliveryRequest;
import com.nowayback.delivery.presentation.dto.request.UpdateDeliveryRecipientInfoRequest;
import com.nowayback.delivery.presentation.dto.request.UpdateDeliveryStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.nowayback.delivery.fixture.DeliveryFixture.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(DeliveryController.class)
class DeliveryControllerTest extends ControllerTest {

    @MockitoBean
    private DeliveryService deliveryService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/deliveries";

    @Nested
    @DisplayName("배송 생성 API")
    class CreateDelivery {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER"})
        @DisplayName("유효한 요청이 들어오면 배송이 생성된다.")
        void createDelivery_ValidRequest_Success(UserRole role) throws Exception {
            /* given */
            CreateDeliveryRequest request = VALID_CREATE_DELIVERY_REQUEST;
            DeliveryResult result = DELIVERY_RESULT;

            given(deliveryService.createDelivery(any())).willReturn(result);

            /* when */
            /* then */
            performWithAuth(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    role)
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(DeliveryStatus.WAITING_AT_HUB.name()));
        }

        @Test
        @DisplayName("유효하지 않은 요청이 들어오면 응답코드 400을 반환한다.")
        void createDelivery_InvalidRequest_BadRequest() throws Exception {
            /* given */
            CreateDeliveryRequest request = INVALID_CREATE_DELIVERY_REQUEST;

            /* when */
            /* then */
            performWithAuth(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 응답코드 401을 반환한다.")
        void createDelivery_WhenHeaderMissing_Unauthorized() throws Exception {
            /* given */
            CreateDeliveryRequest request = VALID_CREATE_DELIVERY_REQUEST;

            /* when */
            /* then */
            mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"HUB_MANAGER", "DELIVERY_MANAGER", "COMPANY_MANAGER"})
        @DisplayName("권한이 없는 사용자가 요청하면 응답코드 403을 반환한다.")
        void createDelivery_WhenRoleInvalid_Forbidden(UserRole role) throws Exception {
            /* given */
            CreateDeliveryRequest request = VALID_CREATE_DELIVERY_REQUEST;

            /* when */
            /* then */
            performWithAuth(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    role)
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("배송 단일 조회 API")
    class GetDelivery {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER", "HUB_MANAGER", "DELIVERY_MANAGER", "COMPANY_MANAGER"})
        @DisplayName("유효한 요청이 들어오면 배송을 조회한다.")
        void getDelivery_ExistingUuid_Success(UserRole role) throws Exception {
            /* given */
            DeliveryResult result = DELIVERY_RESULT;

            given(deliveryService.getDelivery(DELIVERY_UUID)).willReturn(result);

            /* when */
            /* then */
            performWithAuth(get(BASE_URL + "/" + DELIVERY_UUID), role)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.orderId").value(result.orderId().toString()));
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 응답코드 401을 반환한다.")
        void getDelivery_WhenHeaderMissing_Unauthorized() throws Exception {
            /* given */
            /* when */
            /* then */
            mockMvc.perform(get(BASE_URL + "/" + DELIVERY_UUID))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("배송 검색 API")
    class SearchDeliveries {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER", "HUB_MANAGER", "DELIVERY_MANAGER", "COMPANY_MANAGER"})
        @DisplayName("유효한 요청이 들어오면 배송 목록을 조회한다.")
        void searchDeliveries_ValidRequest_Success(UserRole role) throws Exception {
            /* given */
            given(deliveryService.searchDeliveries(any(), any(), any(), any(), anyInt(), anyInt())).willReturn(DELIVERY_RESULT_PAGE);

            /* when */
            /* then */
            performWithAuth(get(BASE_URL)
                            .param("orderId", ORDER_UUID.toString())
                            .param("sourceHubId", SOURCE_HUB_UUID.toString())
                            .param("destinationHubId", DESTINATION_HUB_UUID.toString())
                            .param("status", DeliveryStatus.WAITING_AT_HUB.name())
                            .param("page", String.valueOf(PAGE))
                            .param("size",  String.valueOf(SIZE)),
                    UserRole.MASTER)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(2))
                    .andExpect(jsonPath("$.totalElements").value(2));
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 응답코드 401을 반환한다.")
        void searchDeliveries_WhenHeaderMissing_Unauthorized() throws Exception {
            /* given */
            /* when */
            /* then */
            mockMvc.perform(get(BASE_URL)
                            .param("orderId", ORDER_UUID.toString())
                            .param("sourceHubId", SOURCE_HUB_UUID.toString())
                            .param("destinationHubId", DESTINATION_HUB_UUID.toString())
                            .param("status", DeliveryStatus.WAITING_AT_HUB.name())
                            .param("page", String.valueOf(PAGE))
                            .param("size",  String.valueOf(SIZE)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("배송 수령인 정보 수정 API")
    class UpdateDeliveryRecipientInfo {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER", "HUB_MANAGER", "DELIVERY_MANAGER"})
        @DisplayName("유효한 요청이 들어오면 배송 수령인 정보를 수정한다.")
        void updateDeliveryRecipientInfo_ValidRequest_Success(UserRole role) throws Exception {
            /* given */
            UpdateDeliveryRecipientInfoRequest request = VALID_UPDATE_DELIVERY_RECIPIENT_INFO_REQUEST;
            DeliveryResult result = MODIFIED_DELIVERY_RESULT;

            given(deliveryService.updateRecipientInfo(eq(DELIVERY_UUID), any())).willReturn(result);

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/" + DELIVERY_UUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    role)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.recipientName").value(request.recipientName()));
        }

        @Test
        @DisplayName("유효하지 않은 요청이 들어오면 응답코드 400을 반환한다.")
        void updateDeliveryRecipientInfo_InvalidRequest_BadRequest() throws Exception {
            /* given */
            UpdateDeliveryRecipientInfoRequest request = INVALID_UPDATE_DELIVERY_RECIPIENT_INFO_REQUEST;

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/" + DELIVERY_UUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    UserRole.MASTER)
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 응답코드 401을 반환한다.")
        void updateDeliveryRecipientInfo_WhenHeaderMissing_Unauthorized() throws Exception {
            /* given */
            /* when */
            /* then */
            mockMvc.perform(patch(BASE_URL + "/" + DELIVERY_UUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(VALID_UPDATE_DELIVERY_RECIPIENT_INFO_REQUEST)))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"COMPANY_MANAGER"})
        @DisplayName("권한이 없는 사용자가 요청하면 응답코드 403을 반환한다.")
        void updateDeliveryRecipientInfo_WhenRoleInvalid_Forbidden(UserRole role) throws Exception {
            /* given */
            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/" + DELIVERY_UUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(VALID_UPDATE_DELIVERY_RECIPIENT_INFO_REQUEST)),
                    role)
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("배송 상태 수정 API")
    class UpdateDeliveryStatus {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER", "HUB_MANAGER", "DELIVERY_MANAGER"})
        @DisplayName("배송 상태 수정 요청이 들어오면 배송 상태를 수정한다.")
        void updateDeliveryStatus_ValidRequest_Success(UserRole role) throws Exception {
            /* given */
            UpdateDeliveryStatusRequest request = VALID_UPDATE_DELIVERY_STATUS_REQUEST;
            DeliveryResult result = DELIVERY_RESULT_TRANSIT_BETWEEN_HUBS;

            given(deliveryService.updateDeliveryStatus(eq(DELIVERY_UUID), any())).willReturn(result);

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/" + DELIVERY_UUID + "/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    role)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(DeliveryStatus.TRANSIT_BETWEEN_HUBS.name()));
        }

        @Test
        @DisplayName("유효하지 않은 요청이 들어오면 응답코드 400을 반환한다.")
        void updateDeliveryStatus_InvalidRequest_BadRequest() throws Exception {
            /* given */
            UpdateDeliveryStatusRequest request = INVALID_UPDATE_DELIVERY_STATUS_REQUEST;

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/" + DELIVERY_UUID + "/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    UserRole.MASTER)
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 응답코드 401을 반환한다.")
        void updateDeliveryStatus_WhenHeaderMissing_Unauthorized() throws Exception {
            /* given */
            /* when */
            /* then */
            mockMvc.perform(patch(BASE_URL + "/" + DELIVERY_UUID + "/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(VALID_UPDATE_DELIVERY_STATUS_REQUEST)))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"COMPANY_MANAGER"})
        @DisplayName("권한이 없는 사용자가 요청하면 응답코드 403을 반환한다.")
        void updateDeliveryStatus_WhenRoleInvalid_Forbidden(UserRole role) throws Exception {
            /* given */
            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/" + DELIVERY_UUID + "/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(VALID_UPDATE_DELIVERY_STATUS_REQUEST)),
                    role)
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("배송 삭제 API")
    class DeleteDelivery {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER", "HUB_MANAGER"})
        @DisplayName("배송 삭제 요청이 들어오면 배송을 삭제한다.")
        void deleteDelivery_ValidRequest_Success(UserRole role) throws Exception {
            /* given */
            /* when */
            /* then */
            performWithAuth(delete(BASE_URL + "/" + DELIVERY_UUID), role)
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 응답코드 401을 반환한다.")
        void deleteDelivery_WhenHeaderMissing_Unauthorized() throws Exception {
            /* given */
            /* when */
            /* then */
            mockMvc.perform(delete(BASE_URL + "/" + DELIVERY_UUID))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"DELIVERY_MANAGER", "COMPANY_MANAGER"})
        @DisplayName("권한이 없는 사용자가 요청하면 응답코드 403을 반환한다.")
        void deleteDelivery_WhenRoleInvalid_Forbidden(UserRole role) throws Exception {
            /* given */
            /* when */
            /* then */
            performWithAuth(delete(BASE_URL + "/" + DELIVERY_UUID), role)
                    .andExpect(status().isForbidden());
        }
    }
}