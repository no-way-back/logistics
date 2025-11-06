package com.nowayback.hub.application;

import com.nowayback.hub.application.command.CreateHubCommand;
import com.nowayback.hub.application.dto.HubResult;
import com.nowayback.hub.domain.entity.HubEntity;
import com.nowayback.hub.domain.repository.HubRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

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

            HubEntity savedHub = HubEntity.create(command);

            when(hubRepository.existsByName(command.name())).thenReturn(false);
            when(hubRepository.save(any(HubEntity.class))).thenReturn(savedHub);

            // when
            HubResult result = hubService.create(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo("서울특별시 센터");
            assertThat(result.address()).isEqualTo("서울시 송파구 송파대로 55");
            assertThat(result.latitude()).isEqualByComparingTo("37.5665");
            assertThat(result.longitude()).isEqualByComparingTo("126.9780");

            verify(hubRepository, times(1)).existsByName(command.name());
            verify(hubRepository, times(1)).save(any(HubEntity.class));
        }

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
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 존재하는 허브 이름입니다.");

            verify(hubRepository, times(1)).existsByName(command.name());
            verify(hubRepository, never()).save(any(HubEntity.class));
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

            when(hubRepository.existsByAddress(command.address())).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> hubService.create(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 존재하는 허브 주소입니다.");

            verify(hubRepository, times(1)).existsByAddress(command.address());
            verify(hubRepository, never()).save(any(HubEntity.class));
        }
    }
}