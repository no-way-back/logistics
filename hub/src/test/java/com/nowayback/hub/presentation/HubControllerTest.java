package com.nowayback.hub.presentation;

import com.nowayback.hub.application.HubService;
import com.nowayback.hub.application.command.CreateHubCommand;
import com.nowayback.hub.application.dto.HubResult;
import com.nowayback.hub.application.exception.HubApplicationException;
import com.nowayback.hub.domain.entity.Hub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static com.nowayback.hub.application.exception.HubApplicationErrorCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("POST /hubs - 허브 생성 성공 테스트")
    class HubCreateSuccess {

        @Test
        @DisplayName("유효한 요청으로 허브를 생성할 수 있다")
        void create_hub_success() throws Exception {
            // given
            String requestBody = """
                {
                    "name": "서울특별시 센터",
                    "address": "서울시 송파구 송파대로 55",
                    "latitude": 37.5665,
                    "longitude": 126.9780
                }
                """;

            HubResult hubResult = HubResult.from(
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
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("서울특별시 센터"));

            verify(hubService, times(1)).create(any(CreateHubCommand.class));
        }
    }

    @Nested
    @DisplayName("POST /hubs - 허브 생성 실패 테스트")
    class HubCreateFailure {

        @Test
        @DisplayName("이름이 중복되면 409 에러를 반환한다")
        void create_hub_with_duplicate_name_returns_bad_request() throws Exception {
            // given
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
                            .content(requestBody))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("주소가 중복되면 409 에러를 반환한다")
        void create_hub_with_duplicate_address_returns_bad_request() throws Exception {
            // given
            String requestBody = """
        {
            "name": "경기도 센터",
            "address": "서울시 송파구 송파대로 55",
            "latitude": 37.5665,
            "longitude": 126.9780
        }
        """;

            when(hubService.create(any(CreateHubCommand.class)))
                    .thenThrow(new HubApplicationException(HUB_ADDRESS_ALEADY_EXISTS_EXCEPTION));

            // when & then
            mockMvc.perform(post("/hubs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isConflict());

            verify(hubService, times(1)).create(any(CreateHubCommand.class));
        }
    }
}