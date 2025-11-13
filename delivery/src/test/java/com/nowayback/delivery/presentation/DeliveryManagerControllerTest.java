package com.nowayback.delivery.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.delivery.application.deliverymanager.DeliveryManagerService;
import com.nowayback.delivery.application.deliverymanager.dto.DeliveryManagerResult;
import com.nowayback.delivery.presentation.dto.request.CreateDeliveryManagerRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.nowayback.delivery.fixture.DeliveryManagerFixture.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(DeliveryManagerController.class)
@DisplayName("배송 담당자 컨트롤러")
class DeliveryManagerControllerTest extends ControllerTest {

    @MockitoBean
    private DeliveryManagerService deliveryManagerService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/delivery-managers";

    @Nested
    @DisplayName("배송 담당자 생성 API")
    class CreateDeliveryManager {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER"})
        @DisplayName("유효한 요청이 들어오면 배송 담당자를 생성된다.")
        void createDeliveryManager_Success(UserRole role) throws Exception {
            /* given */
            CreateDeliveryManagerRequest request = CREATE_DELIVERY_MANAGER_REQUEST;;
            DeliveryManagerResult result = DELIVERY_MANAGER_RESULT;

            given(deliveryManagerService.createDeliveryManager(any())).willReturn(result);

            /* when */
            /* then */
            performWithAuth(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    role)
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.deliveryManagerId").value(result.deliveryManagerId().toString()));
        }

        @Test
        @DisplayName("유효하지 않은 요청이 들어오면 400 에러를 반환한다.")
        void createDeliveryManager_InvalidRequest_BadRequest() throws Exception {
            /* given */
            CreateDeliveryManagerRequest request = INVALID_CREATE_DELIVERY_MANAGER_REQUEST;

            /* when */
            /* then */
            performWithAuth(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 401 에러를 반환한다.")
        void createDeliveryManager_WhenHeaderMissing_Unauthorized() throws Exception {
            /* given */
            CreateDeliveryManagerRequest request = CREATE_DELIVERY_MANAGER_REQUEST;

            /* when */
            /* then */
            mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"HUB_MANAGER", "DELIVERY_MANAGER", "COMPANY_MANAGER"})
        @DisplayName("권한이 없는 사용자가 요청하면 403 에러를 반환한다.")
        void createDeliveryManager_WhenRoleInvalid_Forbidden(UserRole role) throws Exception {
            /* given */
            CreateDeliveryManagerRequest request = CREATE_DELIVERY_MANAGER_REQUEST;

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
    @DisplayName("배송 담당자 삭제 API")
    class DeleteDeliveryManager {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER"})
        @DisplayName("유효한 요청이 들어오면 배송 담당자를 삭제한다.")
        void deleteDeliveryManager_Success(UserRole role) throws Exception {
            /* given */
            /* when */
            /* then */
            performWithAuth(delete(BASE_URL)
                            .param("deliveryManagerId", DELIVERY_MANAGER_UUID.toString()),
                    role)
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 401 에러를 반환한다.")
        void deleteDeliveryManager_WhenHeaderMissing_Unauthorized() throws Exception {
            /* given */
            /* when */
            /* then */
            mockMvc.perform(delete(BASE_URL)
                            .param("deliveryManagerId", DELIVERY_MANAGER_UUID.toString()))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"HUB_MANAGER", "DELIVERY_MANAGER", "COMPANY_MANAGER"})
        @DisplayName("권한이 없는 사용자가 요청하면 403 에러를 반환한다.")
        void deleteDeliveryManager_WhenRoleInvalid_Forbidden(UserRole role) throws Exception {
            /* given */
            /* when */
            /* then */
            performWithAuth(delete(BASE_URL)
                            .param("deliveryManagerId", DELIVERY_MANAGER_UUID.toString()),
                    role)
                    .andExpect(status().isForbidden());
        }
    }
}