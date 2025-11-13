package com.nowayback.delivery.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.delivery.application.deliveryroute.DeliveryRouteService;
import com.nowayback.delivery.application.deliveryroute.dto.DeliveryRouteResult;
import com.nowayback.delivery.fixture.DeliveryRouteFixture;
import com.nowayback.delivery.presentation.dto.request.UpdateDeliveryRouteInfoRequest;
import com.nowayback.delivery.presentation.dto.request.UpdateDeliveryRouteStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.nowayback.delivery.fixture.DeliveryRouteFixture.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(DeliveryRouteController.class)
@DisplayName("배송 경로 컨트롤러")
class DeliveryRouteControllerTest extends ControllerTest {

    @MockitoBean
    private DeliveryRouteService deliveryRouteService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/delivery-routes";

    @Nested
    @DisplayName("배송 경로 단일 조회 API")
    class GetDeliveryRoute {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER", "HUB_MANAGER", "DELIVERY_MANAGER", "COMPANY_MANAGER"})
        @DisplayName("유효한 요청이 들어오면 배송 경로를 조회한다.")
        void getDeliveryRoute_Success(UserRole role) throws Exception {
            /* given */
            DeliveryRouteResult result = DELIVERY_ROUTE_RESULT;

            given(deliveryRouteService.getDelivery(any(), eq(role), eq(DELIVERY_ROUTE_UUID))).willReturn(result);

            /* when */
            /* then */
            performWithAuth(get(BASE_URL + "/{deliveryRouteId}", DELIVERY_ROUTE_UUID), role)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sequence").value(SEQUENCE))
                    .andExpect(jsonPath("$.sourceHubId").value(SOURCE_HUB_UUID.toString()))
                    .andExpect(jsonPath("$.destinationHubId").value(DESTINATION_HUB_UUID.toString()))
                    .andExpect(jsonPath("$.hubDeliveryManagerId").value(DELIVERY_MANAGER_UUID.toString()))
                    .andExpect(jsonPath("$.expectedDistanceMeters").value(EXPECTED_DISTANCE_METERS))
                    .andExpect(jsonPath("$.expectedDurationMinutes").value(EXPECTED_DURATION_MINUTES))
                    .andExpect(jsonPath("$.actualDistanceMeters").isEmpty())
                    .andExpect(jsonPath("$.actualDurationMinutes").isEmpty());
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 응답코드 401을 반환한다.")
        void getDeliveryRoute_Unauthenticated_Failure() throws Exception {
            /* given */
            /* when */
            /* then */
            mockMvc.perform(get(BASE_URL + "/{deliveryRouteId}", DELIVERY_ROUTE_UUID))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("배송 경로 검색 API")
    class SearchDeliveryRoutes {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER", "HUB_MANAGER", "DELIVERY_MANAGER", "COMPANY_MANAGER"})
        @DisplayName("유효한 요청이 들어오면 배송 경로 목록을 조회한다.")
        void searchDeliveryRoutes_Success(UserRole role) throws Exception {
            /* given */
            Pageable pageable = PAGEABLE;

            given(deliveryRouteService.searchDeliveryRoutes(any(), eq(role), eq(DELIVERY_UUID), eq(pageable)))
                    .willReturn(DeliveryRouteFixture.DELIVERY_ROUTE_RESULT_PAGE);

            /* when */
            /* then */
            performWithAuth(get(BASE_URL)
                            .param("deliveryId", DELIVERY_UUID.toString())
                            .param("page", String.valueOf(pageable.getPageNumber()))
                            .param("size", String.valueOf(pageable.getPageSize()))
                            .param("sort", "sequence,asc"),
                    role)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.items.length()").value(1))
                    .andExpect(jsonPath("$.items[0].sequence").value(SEQUENCE))
                    .andExpect(jsonPath("$.items[0].sourceHubId").value(SOURCE_HUB_UUID.toString()))
                    .andExpect(jsonPath("$.items[0].destinationHubId").value(DESTINATION_HUB_UUID.toString()))
                    .andExpect(jsonPath("$.items[0].hubDeliveryManagerId").value(DELIVERY_MANAGER_UUID.toString()))
                    .andExpect(jsonPath("$.items[0].expectedDistanceMeters").value(EXPECTED_DISTANCE_METERS))
                    .andExpect(jsonPath("$.items[0].expectedDurationMinutes").value(EXPECTED_DURATION_MINUTES))
                    .andExpect(jsonPath("$.items[0].actualDistanceMeters").isEmpty())
                    .andExpect(jsonPath("$.items[0].actualDurationMinutes").isEmpty());
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 응답코드 401을 반환한다.")
        void searchDeliveryRoutes_Unauthenticated_Failure() throws Exception {
            /* given */
            /* when */
            /* then */
            mockMvc.perform(get(BASE_URL)
                            .param("deliveryId", DELIVERY_UUID.toString())
                            .param("page", String.valueOf(PAGE))
                            .param("size", String.valueOf(SIZE))
                            .param("sort", "sequence,asc"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("배송 경로 상태 수정 API")
    class UpdateDeliveryRouteStatus {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER", "DELIVERY_MANAGER"})
        @DisplayName("유효한 요청이 들어오면 배송 경로 상태를 수정한다.")
        void updateDeliveryRouteStatus_Success(UserRole role) throws Exception {
            /* given */
            UpdateDeliveryRouteStatusRequest request = UPDATE_DELIVERY_ROUTE_STATUS_REQUEST;
            DeliveryRouteResult result = MODIFIED_STATUS_DELIVERY_ROUTE_RESULT;

            given(deliveryRouteService.updateDeliveryRouteStatus(any(), eq(role), eq(DELIVERY_ROUTE_UUID), any()))
                    .willReturn(result);

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/{deliveryRouteId}/status", DELIVERY_ROUTE_UUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    role)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sequence").value(SEQUENCE))
                    .andExpect(jsonPath("$.sourceHubId").value(SOURCE_HUB_UUID.toString()))
                    .andExpect(jsonPath("$.destinationHubId").value(DESTINATION_HUB_UUID.toString()))
                    .andExpect(jsonPath("$.hubDeliveryManagerId").value(DELIVERY_MANAGER_UUID.toString()))
                    .andExpect(jsonPath("$.status").value(request.deliveryRouteStatus().toString()))
                    .andExpect(jsonPath("$.expectedDistanceMeters").value(EXPECTED_DISTANCE_METERS))
                    .andExpect(jsonPath("$.expectedDurationMinutes").value(EXPECTED_DURATION_MINUTES))
                    .andExpect(jsonPath("$.actualDistanceMeters").isEmpty())
                    .andExpect(jsonPath("$.actualDurationMinutes").isEmpty());
        }

        @Test
        @DisplayName("유효하지 않은 요청이 들어오면 응답코드 400을 반환한다.")
        void updateDeliveryRouteStatus_InvalidRequest_Failure() throws Exception {
            /* given */
            UpdateDeliveryRouteStatusRequest request = INVALID_UPDATE_DELIVERY_ROUTE_STATUS_REQUEST;

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/{deliveryRouteId}/status", DELIVERY_ROUTE_UUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 응답코드 401을 반환한다.")
        void updateDeliveryRouteStatus_Unauthenticated_Failure() throws Exception {
            /* given */
            /* when */
            /* then */
            mockMvc.perform(patch(BASE_URL + "/{deliveryRouteId}/status", DELIVERY_ROUTE_UUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(UPDATE_DELIVERY_ROUTE_STATUS_REQUEST)))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"HUB_MANAGER", "COMPANY_MANAGER"})
        @DisplayName("권한이 없는 사용자가 요청하면 응답코드 403을 반환한다.")
        void updateDeliveryRouteStatus_Forbidden_Failure(UserRole role) throws Exception {
            /* given */
            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/{deliveryRouteId}/status", DELIVERY_ROUTE_UUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(UPDATE_DELIVERY_ROUTE_STATUS_REQUEST)),
                    role)
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("배송 경로 정보 수정 API")
    class UpdateDeliveryRouteInfo {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER", "DELIVERY_MANAGER"})
        @DisplayName("유효한 요청이 들어오면 배송 경로 정보를 수정한다.")
        void updateDeliveryRouteInfo_Success() throws Exception {
            /* given */
            UpdateDeliveryRouteInfoRequest request = UPDATE_DELIVERY_ROUTE_INFO_REQUEST;
            DeliveryRouteResult result = getModifiedInfoDeliveryRouteResult();

            given(deliveryRouteService.updateDeliveryRouteInfo(any(), eq(UserRole.DELIVERY_MANAGER), eq(DELIVERY_ROUTE_UUID), any()))
                    .willReturn(result);

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/{deliveryRouteId}", DELIVERY_ROUTE_UUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    UserRole.DELIVERY_MANAGER)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sequence").value(SEQUENCE))
                    .andExpect(jsonPath("$.sourceHubId").value(SOURCE_HUB_UUID.toString()))
                    .andExpect(jsonPath("$.destinationHubId").value(DESTINATION_HUB_UUID.toString()))
                    .andExpect(jsonPath("$.hubDeliveryManagerId").value(DELIVERY_MANAGER_UUID.toString()))
                    .andExpect(jsonPath("$.expectedDistanceMeters").value(EXPECTED_DISTANCE_METERS))
                    .andExpect(jsonPath("$.expectedDurationMinutes").value(EXPECTED_DURATION_MINUTES))
                    .andExpect(jsonPath("$.actualDistanceMeters").value(request.actualDistanceMeters()))
                    .andExpect(jsonPath("$.actualDurationMinutes").value(request.actualDurationMinutes()));
        }

        @Test
        @DisplayName("유효하지 않은 요청이 들어오면 응답코드 400을 반환한다.")
        void updateDeliveryRouteInfo_InvalidRequest_Failure() throws Exception {
            /* given */
            UpdateDeliveryRouteInfoRequest request = INVALID_UPDATE_DELIVERY_ROUTE_INFO_REQUEST;

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/{deliveryRouteId}", DELIVERY_ROUTE_UUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    UserRole.DELIVERY_MANAGER)
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 응답코드 401을 반환한다.")
        void updateDeliveryRouteInfo_Unauthenticated_Failure() throws Exception {
            /* given */
            /* when */
            /* then */
            mockMvc.perform(patch(BASE_URL + "/{deliveryRouteId}", DELIVERY_ROUTE_UUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(UPDATE_DELIVERY_ROUTE_INFO_REQUEST)))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"HUB_MANAGER", "COMPANY_MANAGER"})
        @DisplayName("권한이 없는 사용자가 요청하면 응답코드 403을 반환한다.")
        void updateDeliveryRouteInfo_Forbidden_Failure(UserRole role) throws Exception {
            /* given */
            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/{deliveryRouteId}", DELIVERY_ROUTE_UUID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(UPDATE_DELIVERY_ROUTE_INFO_REQUEST)),
                    role)
                    .andExpect(status().isForbidden());
        }
    }
}