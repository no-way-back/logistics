package com.nowayback.delivery.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.delivery.application.DeliveryService;
import com.nowayback.delivery.application.dto.DeliveryResult;
import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;
import com.nowayback.delivery.presentation.dto.request.CreateDeliveryRequest;
import com.nowayback.delivery.presentation.dto.request.UpdateDeliveryRecipientInfoRequest;
import com.nowayback.delivery.presentation.dto.request.UpdateDeliveryStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

        @Test
        @DisplayName("유효한 요청이 들어오면 배송이 생성된다.")
        void createDelivery_ValidRequest_Success() throws Exception {
            /* given */
            CreateDeliveryRequest request = VALID_CREATE_DELIVERY_REQUEST;
            DeliveryResult result = DELIVERY_RESULT;

            given(deliveryService.createDelivery(any())).willReturn(result);

            /* when */
            /* then */
            performWithAuth(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    UserRole.MASTER)
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
                            .content(objectMapper.writeValueAsString(request)),
                    UserRole.MASTER)
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("배송 단일 조회 API")
    class GetDelivery {

        @Test
        @DisplayName("유효한 요청이 들어오면 배송을 조회한다.")
        void getDelivery_ExistingUuid_Success() throws Exception {
            /* given */
            DeliveryResult result = DELIVERY_RESULT;

            given(deliveryService.getDelivery(DELIVERY_UUID)).willReturn(result);

            /* when */
            /* then */
            performWithAuth(get(BASE_URL + "/" + DELIVERY_UUID), UserRole.MASTER)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.orderId").value(result.orderId().toString()));
        }
    }

    @Nested
    @DisplayName("배송 검색 API")
    class SearchDeliveries {

        @Test
        @DisplayName("유효한 요청이 들어오면 배송 목록을 조회한다.")
        void searchDeliveries_ValidRequest_Success() throws Exception {
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
    }

    @Nested
    @DisplayName("배송 수령인 정보 수정 API")
    class UpdateDeliveryRecipientInfo {

        @Test
        @DisplayName("유효한 요청이 들어오면 배송 수령인 정보를 수정한다.")
        void updateDeliveryRecipientInfo_ValidRequest_Success() throws Exception {
            /* given */
            UpdateDeliveryRecipientInfoRequest request = VALID_UPDATE_DELIVERY_RECIPIENT_INFO_REQUEST;
            DeliveryResult result = MODIFIED_DELIVERY_RESULT;

            given(deliveryService.updateRecipientInfo(eq(DELIVERY_UUID), any())).willReturn(result);

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/" + DELIVERY_UUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    UserRole.MASTER)
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
    }

    @Nested
    @DisplayName("배송 상태 수정 API")
    class UpdateDeliveryStatus {

        @Test
        @DisplayName("배송 상태 수정 요청이 들어오면 배송 상태를 수정한다.")
        void updateDeliveryStatus_ValidRequest_Success() throws Exception {
            /* given */
            UpdateDeliveryStatusRequest request = VALID_UPDATE_DELIVERY_STATUS_REQUEST;
            DeliveryResult result = DELIVERY_RESULT_TRANSIT_BETWEEN_HUBS;

            given(deliveryService.updateDeliveryStatus(eq(DELIVERY_UUID), any())).willReturn(result);

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/" + DELIVERY_UUID + "/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    UserRole.MASTER)
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
    }

    @Nested
    @DisplayName("배송 삭제 API")
    class DeleteDelivery {

        @Test
        @DisplayName("배송 삭제 요청이 들어오면 배송을 삭제한다.")
        void deleteDelivery_ValidRequest_Success() throws Exception {
            /* given */
            /* when */
            /* then */
            performWithAuth(delete(BASE_URL + "/" + DELIVERY_UUID), UserRole.MASTER)
                    .andExpect(status().isNoContent());
        }
    }
}