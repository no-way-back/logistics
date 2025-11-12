package com.nowayback.hub.presentation.hub;

import com.nowayback.common.security.annotation.UserRole;
import com.nowayback.common.security.interceptor.JwtConstants;
import com.nowayback.hub.application.hub.HubService;
import com.nowayback.hub.application.hub.command.CreateHubCommand;
import com.nowayback.hub.application.hub.command.UpdateHubCommand;
import com.nowayback.hub.application.hub.dto.CreateHubResult;
import com.nowayback.hub.application.hub.dto.GetHubResult;
import com.nowayback.hub.application.hub.dto.UpdateHubResult;
import com.nowayback.hub.application.hub.exception.HubApplicationException;
import com.nowayback.hub.domain.hub.entity.Hub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.nowayback.hub.application.hub.exception.HubApplicationErrorCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HubController.class)
@DisplayName("HubController 테스트")
class HubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HubService hubService;

    @Nested
    @DisplayName("GET /hubs/{hubId} - 허브 단건 조회 테스트")
    class GetHub {

        @Nested
        @DisplayName("허브 조회 성공 테스트")
        class GetHubSuccess {

            @Test
            @DisplayName("유효한 hubId로 허브를 조회할 수 있다")
            void get_hub_success() throws Exception {
                // given
                UUID hubId = UUID.randomUUID();
                UUID createdBy = UUID.randomUUID();
                UUID updatedBy = UUID.randomUUID();
                LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
                LocalDateTime updatedAt = LocalDateTime.now();

                GetHubResult getHubResult = new GetHubResult(
                        hubId,
                        "서울특별시 센터",
                        "서울시 송파구 송파대로 55",
                        new BigDecimal("37.5665"),
                        new BigDecimal("126.9780"),
                        createdAt,
                        createdBy,
                        updatedAt,
                        updatedBy
                );

                when(hubService.getHub(eq(hubId))).thenReturn(getHubResult);

                // when & then
                mockMvc.perform(get("/hubs/{hubId}", hubId))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.hubId").value(hubId.toString()))
                        .andExpect(jsonPath("$.name").value("서울특별시 센터"))
                        .andExpect(jsonPath("$.address").value("서울시 송파구 송파대로 55"))
                        .andExpect(jsonPath("$.latitude").value(37.5665))
                        .andExpect(jsonPath("$.longitude").value(126.9780))
                        .andExpect(jsonPath("$.createdAt").exists())
                        .andExpect(jsonPath("$.createdBy").value(createdBy.toString()))
                        .andExpect(jsonPath("$.updatedAt").exists())
                        .andExpect(jsonPath("$.updatedBy").value(updatedBy.toString()));

                verify(hubService, times(1)).getHub(eq(hubId));
            }

            @Test
            @DisplayName("인증 헤더 없이도 허브를 조회할 수 있다")
            void get_hub_without_auth_header_success() throws Exception {
                // given
                UUID hubId = UUID.randomUUID();
                UUID createdBy = UUID.randomUUID();

                GetHubResult getHubResult = new GetHubResult(
                        hubId,
                        "서울특별시 센터",
                        "서울시 송파구 송파대로 55",
                        new BigDecimal("37.5665"),
                        new BigDecimal("126.9780"),
                        LocalDateTime.now(),
                        createdBy,
                        null,
                        null
                );

                when(hubService.getHub(eq(hubId))).thenReturn(getHubResult);

                // when & then - 헤더 없이 요청
                mockMvc.perform(get("/hubs/{hubId}", hubId))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.hubId").value(hubId.toString()))
                        .andExpect(jsonPath("$.name").value("서울특별시 센터"));

                verify(hubService, times(1)).getHub(eq(hubId));
            }

            @Test
            @DisplayName("일반 사용자도 허브를 조회할 수 있다")
            void get_hub_with_any_role_success() throws Exception {
                // given
                UUID hubId = UUID.randomUUID();
                UUID userId = UUID.randomUUID();

                GetHubResult getHubResult = new GetHubResult(
                        hubId,
                        "서울특별시 센터",
                        "서울시 송파구 송파대로 55",
                        new BigDecimal("37.5665"),
                        new BigDecimal("126.9780"),
                        LocalDateTime.now(),
                        UUID.randomUUID(),
                        null,
                        null
                );

                when(hubService.getHub(eq(hubId))).thenReturn(getHubResult);

                // when & then - HUB_MANAGER 권한으로 요청
                mockMvc.perform(get("/hubs/{hubId}", hubId)
                                .header(JwtConstants.HEADER_USER_ID, userId.toString())
                                .header(JwtConstants.HEADER_USERNAME, "user")
                                .header(JwtConstants.HEADER_ROLE, UserRole.HUB_MANAGER.name()))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.hubId").value(hubId.toString()));

                verify(hubService, times(1)).getHub(eq(hubId));
            }
        }

        @Nested
        @DisplayName("허브 조회 실패 테스트")
        class GetHubFailure {

            @Test
            @DisplayName("존재하지 않는 hubId일 경우 404 에러를 반환한다")
            void get_hub_with_non_existent_id_returns_not_found() throws Exception {
                // given
                UUID hubId = UUID.randomUUID();

                when(hubService.getHub(eq(hubId)))
                        .thenThrow(new HubApplicationException(HUB_NOT_FOUND_EXCEPTION));

                // when & then
                mockMvc.perform(get("/hubs/{hubId}", hubId))
                        .andExpect(status().isNotFound());

                verify(hubService, times(1)).getHub(eq(hubId));
            }

            @Test
            @DisplayName("잘못된 형식의 hubId로 요청 시 400 에러를 반환한다")
            void get_hub_with_invalid_hub_id_format_returns_bad_request() throws Exception {
                // given
                String invalidHubId = "invalid-uuid";

                // when & then
                mockMvc.perform(get("/hubs/{hubId}", invalidHubId))
                        .andExpect(status().isBadRequest());

                verify(hubService, never()).getHub(any(UUID.class));
            }
        }
    }

    @Nested
    @DisplayName("POST /hubs - 허브 생성 테스트")
    class CreateHub {

        @Nested
        @DisplayName("허브 생성 성공 테스트")
        class HubCreateSuccess {

            @Test
            @DisplayName("유효한 요청으로 허브를 생성할 수 있다")
            void create_hub_success() throws Exception {
                // given
                UUID userId = UUID.randomUUID();
                String requestBody = """
                        {
                            "name": "서울특별시 센터",
                            "address": "서울시 송파구 송파대로 55",
                            "latitude": 37.5665,
                            "longitude": 126.9780
                        }
                        """;

                CreateHubResult hubResult = CreateHubResult.of(
                        Hub.create(new CreateHubCommand(
                                "서울특별시 센터",
                                "서울시 송파구 송파대로 55",
                                new BigDecimal("37.5665"),
                                new BigDecimal("126.9780")
                        ))
                );

                when(hubService.create(any(CreateHubCommand.class)))
                        .thenReturn(hubResult);

                // when & then
                mockMvc.perform(post("/hubs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtConstants.HEADER_USER_ID, userId.toString())
                                .header(JwtConstants.HEADER_USERNAME, "admin")
                                .header(JwtConstants.HEADER_ROLE, UserRole.MASTER.name())
                                .content(requestBody))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.name").value("서울특별시 센터"));

                verify(hubService, times(1)).create(any(CreateHubCommand.class));
            }
        }

        @Nested
        @DisplayName("허브 생성 실패 테스트")
        class HubCreateFailure {

            @Test
            @DisplayName("이름이 중복되면 409 에러를 반환한다")
            void create_hub_with_duplicate_name_returns_bad_request() throws Exception {
                // given
                UUID userId = UUID.randomUUID();
                String requestBody = """
                        {
                            "name": "서울특별시 센터",
                            "address": "서울시 송파구 송파대로 55",
                            "latitude": 37.5665,
                            "longitude": 126.9780
                        }
                        """;

                when(hubService.create(any(CreateHubCommand.class)))
                        .thenThrow(new HubApplicationException(HUB_NAME_ALREADY_EXISTS_EXCEPTION));

                // when & then
                mockMvc.perform(post("/hubs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtConstants.HEADER_USER_ID, userId.toString())
                                .header(JwtConstants.HEADER_USERNAME, "admin")
                                .header(JwtConstants.HEADER_ROLE, UserRole.MASTER.name())
                                .content(requestBody))
                        .andExpect(status().isConflict());
            }

            @Test
            @DisplayName("주소가 중복되면 409 에러를 반환한다")
            void create_hub_with_duplicate_address_returns_bad_request() throws Exception {
                // given
                UUID userId = UUID.randomUUID();
                String requestBody = """
                        {
                            "name": "경기도 센터",
                            "address": "서울시 송파구 송파대로 55",
                            "latitude": 37.5665,
                            "longitude": 126.9780
                        }
                        """;

                when(hubService.create(any(CreateHubCommand.class)))
                        .thenThrow(new HubApplicationException(HUB_ADDRESS_ALREADY_EXISTS_EXCEPTION));

                // when & then
                mockMvc.perform(post("/hubs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtConstants.HEADER_USER_ID, userId.toString())
                                .header(JwtConstants.HEADER_USERNAME, "admin")
                                .header(JwtConstants.HEADER_ROLE, UserRole.MASTER.name())
                                .content(requestBody))
                        .andExpect(status().isConflict());

                verify(hubService, times(1)).create(any(CreateHubCommand.class));
            }
        }

        @Nested
        @DisplayName("허브 생성 권한 테스트")
        class HubCreateAuthTest {

            @Test
            @DisplayName("MASTER 권한 없이 허브 생성 시도 시 403 에러를 반환한다")
            void create_hub_without_master_role_returns_forbidden() throws Exception {
                // given
                UUID userId = UUID.randomUUID();
                String requestBody = """
                        {
                            "name": "서울특별시 센터",
                            "address": "서울시 송파구 송파대로 55",
                            "latitude": 37.5665,
                            "longitude": 126.9780
                        }
                        """;

                // when & then
                mockMvc.perform(post("/hubs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtConstants.HEADER_USER_ID, userId.toString())
                                .header(JwtConstants.HEADER_USERNAME, "user")
                                .header(JwtConstants.HEADER_ROLE, UserRole.HUB_MANAGER.name())
                                .content(requestBody))
                        .andExpect(status().isForbidden())
                        .andExpect(jsonPath("$.code").value("FORBIDDEN"));

                verify(hubService, never()).create(any(CreateHubCommand.class));
            }

            @Test
            @DisplayName("인증 헤더 없이 허브 생성 시도 시 401 에러를 반환한다")
            void create_hub_without_auth_header_returns_unauthorized() throws Exception {
                // given
                String requestBody = """
                        {
                            "name": "서울특별시 센터",
                            "address": "서울시 송파구 송파대로 55",
                            "latitude": 37.5665,
                            "longitude": 126.9780
                        }
                        """;

                // when & then
                mockMvc.perform(post("/hubs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                        .andExpect(status().isUnauthorized())
                        .andExpect(jsonPath("$.code").value("UNAUTHORIZED"));

                verify(hubService, never()).create(any(CreateHubCommand.class));
            }

            @Test
            @DisplayName("잘못된 권한 정보로 허브 생성 시도 시 401 에러를 반환한다")
            void create_hub_with_invalid_role_returns_unauthorized() throws Exception {
                // given
                UUID userId = UUID.randomUUID();
                String requestBody = """
                        {
                            "name": "서울특별시 센터",
                            "address": "서울시 송파구 송파대로 55",
                            "latitude": 37.5665,
                            "longitude": 126.9780
                        }
                        """;

                // when & then
                mockMvc.perform(post("/hubs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtConstants.HEADER_USER_ID, userId.toString())
                                .header(JwtConstants.HEADER_USERNAME, "admin")
                                .header(JwtConstants.HEADER_ROLE, "INVALID_ROLE")
                                .content(requestBody))
                        .andExpect(status().isUnauthorized())
                        .andExpect(jsonPath("$.code").value("INVALID_ROLE"));

                verify(hubService, never()).create(any(CreateHubCommand.class));
            }
        }
    }

    @Nested
    @DisplayName("PATCH /hubs/{hubId} - 허브 수정 테스트")
    class UpdateHub {

        @Nested
        @DisplayName("허브 수정 성공 테스트")
        class HubUpdateSuccess {

            @Test
            @DisplayName("유효한 요청으로 허브를 수정할 수 있다")
            void update_hub_success() throws Exception {
                // given
                UUID hubId = UUID.randomUUID();
                UUID userId = UUID.randomUUID();
                LocalDateTime updateAt = LocalDateTime.now();

                String requestBody = """
                        {
                            "name": "서울특별시 센터 (수정)",
                            "address": "서울시 강남구 테헤란로 123",
                            "latitude": 37.5000,
                            "longitude": 127.0000
                        }
                        """;

                UpdateHubResult updateHubResult = new UpdateHubResult(
                        hubId,
                        "서울특별시 센터 (수정)",
                        "서울시 강남구 테헤란로 123",
                        new BigDecimal("37.5000"),
                        new BigDecimal("127.0000"),
                        updateAt,
                        userId
                );

                when(hubService.update(eq(hubId), any(UpdateHubCommand.class)))
                        .thenReturn(updateHubResult);

                // when & then
                mockMvc.perform(patch("/hubs/{hubId}", hubId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtConstants.HEADER_USER_ID, userId.toString())
                                .header(JwtConstants.HEADER_USERNAME, "admin")
                                .header(JwtConstants.HEADER_ROLE, UserRole.MASTER.name())
                                .content(requestBody))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.hubId").value(hubId.toString()))
                        .andExpect(jsonPath("$.name").value("서울특별시 센터 (수정)"))
                        .andExpect(jsonPath("$.address").value("서울시 강남구 테헤란로 123"))
                        .andExpect(jsonPath("$.latitude").value(37.5000))
                        .andExpect(jsonPath("$.longitude").value(127.0000))
                        .andExpect(jsonPath("$.updateAt").exists())
                        .andExpect(jsonPath("$.updatedBy").value(userId.toString()));

                verify(hubService, times(1))
                        .update(eq(hubId), any(UpdateHubCommand.class));
            }
        }

        @Nested
        @DisplayName("허브 수정 실패 테스트")
        class HubUpdateFailure {

            @Test
            @DisplayName("존재하지 않는 hubId일 경우 404 에러를 반환한다")
            void update_hub_with_non_existent_id_returns_not_found() throws Exception {
                // given
                UUID hubId = UUID.randomUUID();
                UUID userId = UUID.randomUUID();

                String requestBody = """
                        {
                            "name": "서울특별시 센터 (수정)",
                            "address": "서울시 강남구 테헤란로 123",
                            "latitude": 37.5000,
                            "longitude": 127.0000
                        }
                        """;

                when(hubService.update(eq(hubId), any(UpdateHubCommand.class)))
                        .thenThrow(new HubApplicationException(HUB_NOT_FOUND_EXCEPTION));

                // when & then
                mockMvc.perform(patch("/hubs/{hubId}", hubId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtConstants.HEADER_USER_ID, userId.toString())
                                .header(JwtConstants.HEADER_USERNAME, "admin")
                                .header(JwtConstants.HEADER_ROLE, UserRole.MASTER.name())
                                .content(requestBody))
                        .andExpect(status().isNotFound());

                verify(hubService, times(1))
                        .update(eq(hubId), any(UpdateHubCommand.class));
            }
        }

        @Nested
        @DisplayName("허브 수정 권한 테스트")
        class HubUpdateAuthTest {

            @Test
            @DisplayName("MASTER 권한 없이 허브 수정 시도 시 403 에러를 반환한다")
            void update_hub_without_master_role_returns_forbidden() throws Exception {
                // given
                UUID hubId = UUID.randomUUID();
                UUID userId = UUID.randomUUID();
                String requestBody = """
                        {
                            "name": "서울특별시 센터 (수정)"
                        }
                        """;

                // when & then
                mockMvc.perform(patch("/hubs/{hubId}", hubId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtConstants.HEADER_USER_ID, userId.toString())
                                .header(JwtConstants.HEADER_USERNAME, "user")
                                .header(JwtConstants.HEADER_ROLE, UserRole.HUB_MANAGER.name())
                                .content(requestBody))
                        .andExpect(status().isForbidden())
                        .andExpect(jsonPath("$.code").value("FORBIDDEN"));

                verify(hubService, never()).update(any(UUID.class), any(UpdateHubCommand.class));
            }

            @Test
            @DisplayName("인증 헤더 없이 허브 수정 시도 시 401 에러를 반환한다")
            void update_hub_without_auth_header_returns_unauthorized() throws Exception {
                // given
                UUID hubId = UUID.randomUUID();
                String requestBody = """
                        {
                            "name": "서울특별시 센터 (수정)"
                        }
                        """;

                // when & then
                mockMvc.perform(patch("/hubs/{hubId}", hubId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                        .andExpect(status().isUnauthorized())
                        .andExpect(jsonPath("$.code").value("UNAUTHORIZED"));

                verify(hubService, never()).update(any(UUID.class), any(UpdateHubCommand.class));
            }
        }
    }

    @Nested
    @DisplayName("DELETE /hubs/{hubId} - 허브 삭제 테스트")
    class DeleteHub {

        @Nested
        @DisplayName("허브 삭제 성공 테스트")
        class HubDeleteSuccess {

            @Test
            @DisplayName("유효한 요청으로 허브를 삭제할 수 있다")
            void delete_hub_success() throws Exception {
                // given
                UUID hubId = UUID.randomUUID();
                UUID userId = UUID.randomUUID();

                doNothing().when(hubService).delete(eq(hubId), eq(userId));

                // when & then
                mockMvc.perform(delete("/hubs/{hubId}", hubId)
                                .header(JwtConstants.HEADER_USER_ID, userId.toString())
                                .header(JwtConstants.HEADER_USERNAME, "admin")
                                .header(JwtConstants.HEADER_ROLE, UserRole.MASTER.name()))
                        .andExpect(status().isNoContent());

                verify(hubService, times(1)).delete(eq(hubId), eq(userId));
            }
        }

        @Nested
        @DisplayName("허브 삭제 실패 테스트")
        class HubDeleteFailure {

            @Test
            @DisplayName("존재하지 않는 hubId일 경우 404 에러를 반환한다")
            void delete_hub_with_non_existent_id_returns_not_found() throws Exception {
                // given
                UUID hubId = UUID.randomUUID();
                UUID userId = UUID.randomUUID();

                doThrow(new HubApplicationException(HUB_NOT_FOUND_EXCEPTION))
                        .when(hubService).delete(eq(hubId), eq(userId));

                // when & then
                mockMvc.perform(delete("/hubs/{hubId}", hubId)
                                .header(JwtConstants.HEADER_USER_ID, userId.toString())
                                .header(JwtConstants.HEADER_USERNAME, "admin")
                                .header(JwtConstants.HEADER_ROLE, UserRole.MASTER.name()))
                        .andExpect(status().isNotFound());

                verify(hubService, times(1)).delete(eq(hubId), eq(userId));
            }
        }

        @Nested
        @DisplayName("허브 삭제 권한 테스트")
        class HubDeleteAuthTest {

            @Test
            @DisplayName("MASTER 권한 없이 허브 삭제 시도 시 403 에러를 반환한다")
            void delete_hub_without_master_role_returns_forbidden() throws Exception {
                // given
                UUID hubId = UUID.randomUUID();
                UUID userId = UUID.randomUUID();

                // when & then
                mockMvc.perform(delete("/hubs/{hubId}", hubId)
                                .header(JwtConstants.HEADER_USER_ID, userId.toString())
                                .header(JwtConstants.HEADER_USERNAME, "user")
                                .header(JwtConstants.HEADER_ROLE, UserRole.HUB_MANAGER.name()))
                        .andExpect(status().isForbidden())
                        .andExpect(jsonPath("$.code").value("FORBIDDEN"));

                verify(hubService, never()).delete(any(UUID.class), any(UUID.class));
            }

            @Test
            @DisplayName("인증 헤더 없이 허브 삭제 시도 시 401 에러를 반환한다")
            void delete_hub_without_auth_header_returns_unauthorized() throws Exception {
                // given
                UUID hubId = UUID.randomUUID();

                // when & then
                mockMvc.perform(delete("/hubs/{hubId}", hubId))
                        .andExpect(status().isUnauthorized())
                        .andExpect(jsonPath("$.code").value("UNAUTHORIZED"));

                verify(hubService, never()).delete(any(UUID.class), any(UUID.class));
            }
        }
    }
}