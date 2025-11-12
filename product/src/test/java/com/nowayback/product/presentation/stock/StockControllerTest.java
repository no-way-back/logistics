package com.nowayback.product.presentation.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.product.application.stock.StockService;
import com.nowayback.product.application.stock.dto.StockResult;
import com.nowayback.product.presentation.ControllerTest;
import com.nowayback.product.presentation.stock.dto.request.DecreaseStockRequest;
import com.nowayback.product.presentation.stock.dto.request.IncreaseStockRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.nowayback.product.fixture.StockFixture.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(StockController.class)
@DisplayName("재고 컨트롤러 테스트")
class StockControllerTest extends ControllerTest {

    @MockitoBean
    private StockService stockService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/stocks";

    @Nested
    @DisplayName("재고 증가 API")
    class IncreaseStock {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER", "HUB_MANAGER"})
        @DisplayName("유효한 요청이 들어오면 재고가 정상적으로 증가된다.")
        void increaseStock_Success(UserRole role) throws Exception {
            /* given */
            IncreaseStockRequest request = VALID_INCREASE_STOCK_REQUEST;
            StockResult result = STOCK_RESULT;

            given(stockService.increaseStock(any())).willReturn(result);

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/{productId}/increase", PRODUCT_UUID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
                    role)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(PRODUCT_UUID.toString()));
        }

        @Test
        @DisplayName("유효하지 않은 요청이 들어오면 응답코드 400을 반환한다.")
        void increaseStock_InvalidRequest_BadRequest() throws Exception {
            /* given */
            IncreaseStockRequest request = INVALID_INCREASE_STOCK_REQUEST;

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/{productId}/increase", PRODUCT_UUID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 응답코드 401을 반환한다.")
        void increaseStock_UnauthenticatedUser_Unauthorized() throws Exception {
            /* given */
            IncreaseStockRequest request = VALID_INCREASE_STOCK_REQUEST;

            /* when */
            /* then */
            mockMvc.perform(patch(BASE_URL + "/{productId}/increase", PRODUCT_UUID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"DELIVERY_MANAGER", "COMPANY_MANAGER"})
        @DisplayName("권한이 없는 사용자가 요청하면 응답코드 403을 반환한다.")
        void increaseStock_ForbiddenUser_Forbidden(UserRole role) throws Exception {
            /* given */
            IncreaseStockRequest request = VALID_INCREASE_STOCK_REQUEST;

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/{productId}/increase", PRODUCT_UUID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
                    role)
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("재고 감소 API")
    class DecreaseStock {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER", "HUB_MANAGER"})
        @DisplayName("유효한 요청이 들어오면 재고가 정상적으로 감소된다.")
        void decreaseStock_Success(UserRole role) throws Exception {
            /* given */
            DecreaseStockRequest request = VALID_DECREASE_STOCK_REQUEST;
            StockResult result = STOCK_RESULT;

            given(stockService.decreaseStock(any())).willReturn(result);

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/{productId}/decrease", PRODUCT_UUID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
                    role)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(PRODUCT_UUID.toString()));
        }

        @Test
        @DisplayName("유효하지 않은 요청이 들어오면 응답코드 400을 반환한다.")
        void decreaseStock_InvalidRequest_BadRequest() throws Exception {
            /* given */
            DecreaseStockRequest request = INVALID_DECREASE_STOCK_REQUEST;

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/{productId}/decrease", PRODUCT_UUID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 응답코드 401을 반환한다.")
        void decreaseStock_UnauthenticatedUser_Unauthorized() throws Exception {
            /* given */
            DecreaseStockRequest request = VALID_DECREASE_STOCK_REQUEST;

            /* when */
            /* then */
            mockMvc.perform(patch(BASE_URL + "/{productId}/decrease", PRODUCT_UUID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"DELIVERY_MANAGER", "COMPANY_MANAGER"})
        @DisplayName("권한이 없는 사용자가 요청하면 응답코드 403을 반환한다.")
        void decreaseStock_ForbiddenUser_Forbidden(UserRole role) throws Exception {
            /* given */
            DecreaseStockRequest request = VALID_DECREASE_STOCK_REQUEST;

            /* when */
            /* then */
            performWithAuth(patch(BASE_URL + "/{productId}/decrease", PRODUCT_UUID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
                    role)
                    .andExpect(status().isForbidden());
        }
    }
}