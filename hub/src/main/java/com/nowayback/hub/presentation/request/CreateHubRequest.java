package com.nowayback.hub.presentation.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateHubRequest(
        @NotBlank(message = "허브 이름은 필수입니다")
        String name,

        @NotBlank(message = "주소는 필수입니다")
        String address,

        @NotNull(message = "위도는 필수입니다")
        @DecimalMin(value = "-90", message = "위도는 -90 이상이어야 합니다")
        @DecimalMax(value = "90", message = "위도는 90 이하여야 합니다")
        BigDecimal latitude,

        @NotNull(message = "경도는 필수입니다")
        @DecimalMin(value = "-180", message = "경도는 -180 이상이어야 합니다")
        @DecimalMax(value = "180", message = "경도는 180 이하여야 합니다")
        BigDecimal longitude
) {}
