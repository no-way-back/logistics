package com.nowayback.delivery.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowayback.delivery.application.DeliveryService;
import com.nowayback.delivery.application.dto.DeliveryResult;
import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;
import com.nowayback.delivery.presentation.dto.request.CreateDeliveryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.nowayback.delivery.fixture.DeliveryFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(DeliveryController.class)
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
            mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
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
            mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
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
            mockMvc.perform(get(BASE_URL + "/" + DELIVERY_UUID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.orderId").value(result.orderId().toString()));
        }
    }
}