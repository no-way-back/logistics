package com.nowayback.hub.presentation.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record UpdateHubRequest(
        String name,

        String address,

        @DecimalMin(value = "-90", message = "위도는 -90 이상이어야 합니다")
        @DecimalMax(value = "90", message = "위도는 90 이하여야 합니다")
        BigDecimal latitude,

        @DecimalMin(value = "-180", message = "경도는 -180 이상이어야 합니다")
        @DecimalMax(value = "180", message = "경도는 180 이하여야 합니다")
        BigDecimal longitude
) {}
