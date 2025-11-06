package com.nowayback.hub.domain;

import com.nowayback.hub.application.command.CreateHubCommand;
import com.nowayback.hub.domain.entity.HubEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class HubEntityTest {

    @Test
    @DisplayName("허브를 생성한다")
    void crete_hub_entity() {
        // given
        String name = "서울특별시 센터";
        String address = "서울특별시 송파구 송파대로 55";
        BigDecimal latitude = new BigDecimal("37.1234567");
        BigDecimal longitude = new BigDecimal("127.1234567");

        CreateHubCommand createHubCommand = new CreateHubCommand(name, address, latitude, longitude);

        // when
        HubEntity hubEntity = HubEntity.create(createHubCommand);

        // then
        assertThat(hubEntity.getName()).isEqualTo(createHubCommand.name());
        assertThat(hubEntity.getAddress()).isEqualTo(address);
        assertThat(hubEntity.getLatitude()).isEqualTo(latitude);
        assertThat(hubEntity.getLongitude()).isEqualTo(longitude);
    }
}