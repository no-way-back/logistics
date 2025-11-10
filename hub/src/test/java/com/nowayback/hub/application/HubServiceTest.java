package com.nowayback.hub.application;

import com.nowayback.hub.application.hub.HubService;
import com.nowayback.hub.application.hub.command.CreateHubCommand;
import com.nowayback.hub.application.hub.command.UpdateHubCommand;
import com.nowayback.hub.application.hub.dto.CreateHubResult;
import com.nowayback.hub.application.hub.dto.UpdateHubResult;
import com.nowayback.hub.application.hub.exception.HubApplicationException;
import com.nowayback.hub.domain.hub.entity.Hub;
import com.nowayback.hub.domain.hub.repository.HubRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HubService 테스트")
class HubServiceTest {

    @Mock
    private HubRepository hubRepository;

    @InjectMocks
    private HubService hubService;

    @Nested
    @DisplayName("허브 생성")
    class CreateHub {

        @Nested
        @DisplayName("허브 생성 성공 테스트")
        class CreateHubSuccess {

            @Test
            @DisplayName("유효한 데이터로 허브를 생성할 수 있다.")
            void create_hub_success() {
                // given
                CreateHubCommand command = new CreateHubCommand(
                        "서울특별시 센터",
                        "서울시 송파구 송파대로 55",
                        new BigDecimal("37.5665"),
                        new BigDecimal("126.9780")
                );

                Hub savedHub = Hub.create(command);

                when(hubRepository.existsByName(command.name())).thenReturn(false);
                when(hubRepository.save(any(Hub.class))).thenReturn(savedHub);

                // when
                CreateHubResult result = hubService.create(command);

                // then
                assertThat(result).isNotNull();
                assertThat(result.name()).isEqualTo("서울특별시 센터");
                assertThat(result.address()).isEqualTo("서울시 송파구 송파대로 55");
                assertThat(result.latitude()).isEqualByComparingTo("37.5665");
                assertThat(result.longitude()).isEqualByComparingTo("126.9780");

                verify(hubRepository, times(1)).existsByName(command.name());
                verify(hubRepository, times(1)).save(any(Hub.class));
            }

        }

        @Nested
        @DisplayName("허브 생성 실패 테스트")
        class CreateHubFailure {

            @Test
            @DisplayName("같은 이름의 허브가 이미 존재하면 예외가 발생한다.")
            void create_hub_with_duplicate_name_throws_exception() {
                // given
                CreateHubCommand command = new CreateHubCommand(
                        "서울특별시 센터",
                        "서울시 송파구 송파대로 55",
                        new BigDecimal("37.5665"),
                        new BigDecimal("126.9780")
                );

                when(hubRepository.existsByName(command.name())).thenReturn(true);

                // when & then
                assertThatThrownBy(() -> hubService.create(command))
                        .isInstanceOf(HubApplicationException.class)
                        .hasFieldOrPropertyWithValue("errorCode", HUB_NAME_ALREADY_EXISTS_EXCEPTION);

                verify(hubRepository, times(1)).existsByName(command.name());
                verify(hubRepository, never()).save(any(Hub.class));
            }

            @Test
            @DisplayName("같은 주소의 허브가 이미 존재하면 예외가 발생한다.")
            void create_hub_with_duplicate_address_throws_exception() {
                // given
                CreateHubCommand command = new CreateHubCommand(
                        "서울특별시 센터",
                        "서울시 송파구 송파대로 55",
                        new BigDecimal("37.5665"),
                        new BigDecimal("126.9780")
                );

                when(hubRepository.existsByName(command.name())).thenReturn(false);
                when(hubRepository.existsByAddress(command.address())).thenReturn(true);

                // when & then
                assertThatThrownBy(() -> hubService.create(command))
                        .isInstanceOf(HubApplicationException.class)
                        .hasFieldOrPropertyWithValue("errorCode", HUB_ADDRESS_ALREADY_EXISTS_EXCEPTION);

                verify(hubRepository, times(1)).existsByName(command.name());
                verify(hubRepository, times(1)).existsByAddress(command.address());
                verify(hubRepository, never()).save(any(Hub.class));
            }
        }
    }


    @Nested
    @DisplayName("허브 수정")
    class UpdateHub {

        @Nested
        @DisplayName("허브 수정 성공 테스트")
        class UpdateHubSuccess {

            @Test
            @DisplayName("유효한 데이터로 허브를 수정할 수 있다")
            void update_hub_success() {
                // given
                UUID hubId = UUID.randomUUID();

                Hub existingHub = Hub.create(new CreateHubCommand(
                        "서울특별시 센터",
                        "서울시 송파구 송파대로 55",
                        new BigDecimal("37.5665"),
                        new BigDecimal("126.9780")
                ));

                UpdateHubCommand command = new UpdateHubCommand(
                        "서울특별시 센터 (수정)",
                        "서울시 강남구 테헤란로 123",
                        new BigDecimal("37.5000"),
                        new BigDecimal("127.0000")
                );

                when(hubRepository.findById(hubId)).thenReturn(Optional.of(existingHub));
                when(hubRepository.existsByName("서울특별시 센터 (수정)")).thenReturn(false);
                when(hubRepository.existsByAddress("서울시 강남구 테헤란로 123")).thenReturn(false);

                // when
                UpdateHubResult result = hubService.update(hubId, command);

                // then
                assertThat(result).isNotNull();
                assertThat(result.name()).isEqualTo("서울특별시 센터 (수정)");
                assertThat(result.address()).isEqualTo("서울시 강남구 테헤란로 123");
                assertThat(result.latitude()).isEqualByComparingTo("37.5000");
                assertThat(result.longitude()).isEqualByComparingTo("127.0000");

                verify(hubRepository, times(1)).findById(hubId);
                verify(hubRepository, times(1)).existsByName("서울특별시 센터 (수정)");
                verify(hubRepository, times(1)).existsByAddress("서울시 강남구 테헤란로 123");
            }
        }

        @Nested
        @DisplayName("허브 수정 실패 테스트")
        class UpdateHubFailure {

            @Test
            @DisplayName("존재하지 않는 허브 ID로 수정 시도하면 예외가 발생한다")
            void update_hub_with_non_existent_id_throws_exception() {
                // given
                UUID hubId = UUID.randomUUID();

                UpdateHubCommand command = new UpdateHubCommand(
                        "서울특별시 센터 (수정)",
                        "서울시 강남구 테헤란로 123",
                        new BigDecimal("37.5000"),
                        new BigDecimal("127.0000")
                );

                when(hubRepository.findById(hubId)).thenReturn(Optional.empty());

                // when & then
                assertThatThrownBy(() -> hubService.update(hubId, command))
                        .isInstanceOf(HubApplicationException.class)
                        .hasFieldOrPropertyWithValue("errorCode", HUB_NOT_FOUND_EXCEPTION);

                verify(hubRepository, times(1)).findById(hubId);
                verify(hubRepository, never()).existsByName(any());
                verify(hubRepository, never()).existsByAddress(any());
            }
        }
    }
}