package com.nowayback.product.presentation.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.product.application.product.ProductService;
import com.nowayback.product.application.product.dto.ProductResult;
import com.nowayback.product.presentation.ControllerTest;
import com.nowayback.product.presentation.product.dto.request.CreateProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static com.nowayback.product.fixture.ProductFixture.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProductController.class)
@DisplayName("상품 컨트롤러 테스트")
class ProductControllerTest extends ControllerTest {

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_UTL = "/products";

    @Nested
    @DisplayName("상품 생성 API")
    class CreateProduct {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER", "HUB_MANAGER"})
        @DisplayName("유효한 요청이 들어오면 상품이 정상적으로 생성된다.")
        void createProduct_Success(UserRole role) throws Exception {
            /* given */
            CreateProductRequest request= VALID_CREATE_PRODUCT_REQUEST;
            ProductResult result = PRODUCT_RESULT;

            given(productService.createProduct(any())).willReturn(result);

            /* when */
            /* then */
            performWithAuth(post(BASE_UTL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
                    role)
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.supplierId").value(result.supplierId().toString()))
                    .andExpect(jsonPath("$.hubId").value(result.hubId().toString()))
                    .andExpect(jsonPath("$.name").value(result.name()))
                    .andExpect(jsonPath("$.price").value(result.price()));
        }

        @Test
        @DisplayName("유효하지 않은 요청이 들어오면 응답코드 400을 반환한다.")
        void createProduct_InvalidRequest_Failure() throws Exception {
            /* given */
            CreateProductRequest request= INVALID_CREATE_PRODUCT_REQUEST;

            /* when */
            /* then */
            performWithAuth(post(BASE_UTL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 응답코드 401을 반환한다.")
        void createProduct_UnauthenticatedUser_Unauthorized() throws Exception {
            /* given */
            CreateProductRequest request= VALID_CREATE_PRODUCT_REQUEST;

            /* when */
            /* then */
            mockMvc.perform(post(BASE_UTL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"DELIVERY_MANAGER", "COMPANY_MANAGER"})
        @DisplayName("권한이 없는 사용자가 요청하면 응답코드 403을 반환한다.")
        void createProduct_ForbiddenUser_Forbidden(UserRole role) throws Exception {
            /* given */
            CreateProductRequest request= VALID_CREATE_PRODUCT_REQUEST;

            /* when */
            /* then */
            performWithAuth(post(BASE_UTL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
                    role)
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("상품 삭제 API")
    class DeleteProduct {

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"MASTER", "HUB_MANAGER"})
        @DisplayName("유효한 요청이 들어오면 상품이 정상적으로 삭제된다.")
        void deleteProduct_Success(UserRole role) throws Exception {
            /* given */
            UUID productId = PRODUCT_ID;

            /* when */
            /* then */
            performWithAuth(delete(BASE_UTL + "/{productId}", productId), role)
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("인증되지 않은 사용자가 요청하면 응답코드 401을 반환한다.")
        void deleteProduct_UnauthenticatedUser_Unauthorized() throws Exception {
            /* given */
            UUID productId = PRODUCT_ID;

            /* when */
            /* then */
            mockMvc.perform(delete(BASE_UTL + "/{productId}", productId))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @EnumSource(value = UserRole.class, names = {"DELIVERY_MANAGER", "COMPANY_MANAGER"})
        @DisplayName("권한이 없는 사용자가 요청하면 응답코드 403을 반환한다.")
        void deleteProduct_ForbiddenUser_Forbidden(UserRole role) throws Exception {
            /* given */
            UUID productId = PRODUCT_ID;

            /* when */
            /* then */
            performWithAuth(delete(BASE_UTL + "/{productId}", productId), role)
                    .andExpect(status().isForbidden());
        }
    }
}