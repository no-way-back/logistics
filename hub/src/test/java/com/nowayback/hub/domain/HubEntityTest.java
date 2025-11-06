package com.nowayback.hub.domain;

import com.nowayback.hub.application.command.CreateHubCommand;
import com.nowayback.hub.domain.entity.HubEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class HubEntityTest {

    @Nested
    @DisplayName("허브 생성 성공 테스트")
    class HubCreateSuccess {

        @Test
        @DisplayName("유효한 값으로 허브를 생성할 수 있다")
        void create_hub_success() {
            // given
            CreateHubCommand command = new CreateHubCommand(
                    "서울특별시 센터",
                    "서울시 송파구 송파대로 55",
                    new BigDecimal("37.5665"),
                    new BigDecimal("126.9780")
            );

            // when
            HubEntity hub = HubEntity.create(command);

            // then
            assertThat(hub).isNotNull();
            assertThat(hub.getName()).isEqualTo("서울특별시 센터");
            assertThat(hub.getAddress()).isEqualTo("서울시 송파구 송파대로 55");
            assertThat(hub.getLatitude()).isEqualByComparingTo("37.5665");
            assertThat(hub.getLongitude()).isEqualByComparingTo("126.9780");
        }
    }

    @Nested
    @DisplayName("유효성 검증 실패")
    class ValidationFailure {

        @Nested
        @DisplayName("이름 검증")
        class NameValidation {

            @Test
            @DisplayName("이름이 null이면 예외가 발생한다")
            void create_hub_with_name_is_null_throw_exception() {
                // given
                CreateHubCommand command = new CreateHubCommand(
                        null,
                        "서울시 송파구 송파대로 55",
                        new BigDecimal("37.5665"),
                        new BigDecimal("126.9780")
                );

                // when & then
                assertThatThrownBy(() -> HubEntity.create(command))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("허브 이름은 필수입니다");
            }

            @Test
            @DisplayName("이름이 빈 문자열이면 예외가 발생한다")
            void create_hub_with_name_empty_string_throw_exception() {
                // given
                CreateHubCommand command = new CreateHubCommand(
                        "",
                        "서울시 송파구 송파대로 55",
                        new BigDecimal("37.5665"),
                        new BigDecimal("126.9780")
                );

                // when & then
                assertThatThrownBy(() -> HubEntity.create(command))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("허브 이름은 필수입니다");
            }
        }

        @Nested
        @DisplayName("주소 검증")
        class AddressValidation {

            @Test
            @DisplayName("주소가 null이면 예외가 발생한다")
            void create_hub_with_address_is_null_throw_exception() {
                // given
                CreateHubCommand command = new CreateHubCommand(
                        "서울특별시 센터",
                        null,
                        new BigDecimal("37.5665"),
                        new BigDecimal("126.9780")
                );

                // when & then
                assertThatThrownBy(() -> HubEntity.create(command))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주소는 필수입니다");
            }

            @Test
            @DisplayName("주소가 빈 문자열이면 예외가 발생한다")
            void create_hub_with_address_empty_string_throw_exception() {
                // given
                CreateHubCommand command = new CreateHubCommand(
                        "서울특별시 센터",
                        "",
                        new BigDecimal("37.5665"),
                        new BigDecimal("126.9780")
                );

                // when & then
                assertThatThrownBy(() -> HubEntity.create(command))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주소는 필수입니다");
            }
        }

        @Nested
        @DisplayName("위도 검증")
        class LatitudeValidation {

            @Test
            @DisplayName("위도가 null이면 예외가 발생한다")
            void create_hub_with_latitude_is_null_throw_exception() {
                // given
                CreateHubCommand command = new CreateHubCommand(
                        "서울특별시 센터",
                        "서울시 송파구 송파대로 55",
                        null,
                        new BigDecimal("126.9780")
                );

                // when & then
                assertThatThrownBy(() -> HubEntity.create(command))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("위도는 필수입니다");
            }

            @Test
            @DisplayName("위도가 유효 범위를 벗어나면 예외가 발생한다 - 최솟값 미만")
            void create_hub_with_latitude_below_valid_range_throws_exception() {
                // given
                CreateHubCommand command = new CreateHubCommand(
                        "서울특별시 센터",
                        "서울시 송파구 송파대로 55",
                        new BigDecimal("-90.1"),
                        new BigDecimal("126.9780")
                );

                // when & then
                assertThatThrownBy(() -> HubEntity.create(command))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("위도는 -90 이상 90 이하여야 합니다");
            }

            @Test
            @DisplayName("위도가 유효 범위를 벗어나면 예외가 발생한다 - 최댓값 초과")
            void create_hub_with_latitude_above_valid_range_throws_exception() {
                // given
                CreateHubCommand command = new CreateHubCommand(
                        "서울특별시 센터",
                        "서울시 송파구 송파대로 55",
                        new BigDecimal("90.1"),
                        new BigDecimal("126.9780")
                );

                // when & then
                assertThatThrownBy(() -> HubEntity.create(command))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("위도는 -90 이상 90 이하여야 합니다");
            }
        }

        @Nested
        @DisplayName("경도 검증")
        class LongitudeValidation {

            @Test
            @DisplayName("경도가 null이면 예외가 발생한다")
            void create_hub_with_longitude_is_null_throw_exception() {
                // given
                CreateHubCommand command = new CreateHubCommand(
                        "서울특별시 센터",
                        "서울시 송파구 송파대로 55",
                        new BigDecimal("37.5665"),
                        null
                );

                // when & then
                assertThatThrownBy(() -> HubEntity.create(command))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("경도는 필수입니다");
            }

            @Test
            @DisplayName("경도가 유효 범위를 벗어나면 예외가 발생한다 - 최솟값 미만")
            void create_hub_with_longitude_below_valid_range_throws_exception() {
                // given
                CreateHubCommand command = new CreateHubCommand(
                        "서울특별시 센터",
                        "서울시 송파구 송파대로 55",
                        new BigDecimal("37.5665"),
                        new BigDecimal("-180.1")
                );

                // when & then
                assertThatThrownBy(() -> HubEntity.create(command))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("경도는 -180 이상 180 이하여야 합니다");
            }

            @Test
            @DisplayName("경도가 유효 범위를 벗어나면 예외가 발생한다 - 최댓값 초과")
            void create_hub_with_longitude_above_valid_range_throws_exception() {
                // given
                CreateHubCommand command = new CreateHubCommand(
                        "서울특별시 센터",
                        "서울시 송파구 송파대로 55",
                        new BigDecimal("37.5665"),
                        new BigDecimal("180.1")
                );

                // when & then
                assertThatThrownBy(() -> HubEntity.create(command))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("경도는 -180 이상 180 이하여야 합니다");
            }
        }
    }
}