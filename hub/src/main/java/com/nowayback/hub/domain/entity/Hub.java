package com.nowayback.hub.domain.entity;

import audit.BaseEntity;
import com.nowayback.hub.application.command.CreateHubCommand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "p_hubs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hub extends BaseEntity {

    private static final BigDecimal MIN_LATITUDE = new BigDecimal("-90");
    private static final BigDecimal MAX_LATITUDE = new BigDecimal("90");
    private static final BigDecimal MIN_LONGITUDE = new BigDecimal("-180");
    private static final BigDecimal MAX_LONGITUDE = new BigDecimal("180");

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    public static Hub create(CreateHubCommand command) {
        validateCommand(command);

        return new Hub(
                command.name(),
                command.address(),
                command.latitude(),
                command.longitude()
        );
    }

    /**
     * 주문 생성 유효성 검새
     */
    private static void validateCommand(CreateHubCommand command) {
        validateName(command.name());
        validateAddress(command.address());
        validateLatitude(command.latitude());
        validateLongitude(command.longitude());
    }

    /**
     * 허브 이름 유효성 검사
     *  - 허브 이름은 필수입니다.
     */
    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("허브 이름은 필수입니다");
        }
    }

    /**
     * 주소 유효성 검사
     *  - 주소는 필수입니다.
     */
    private static void validateAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("주소는 필수입니다");
        }
    }

    /**
     * 위도 유효성 검사
     *  - 위도는 필수입니다.
     *  - 위도는 -90 이상 90 이하여야 합니다.
     */
    private static void validateLatitude(BigDecimal latitude) {
        if (latitude == null) {
            throw new IllegalArgumentException("위도는 필수입니다");
        }
        if (latitude.compareTo(MIN_LATITUDE) < 0 || latitude.compareTo(MAX_LATITUDE) > 0) {
            throw new IllegalArgumentException("위도는 -90 이상 90 이하여야 합니다");
        }
    }

    /**
     * 경도 유효성 검사
     *  - 경도는 필수입니다.
     *  - 경도는 -180 이상 180 이하여야 합니다.
     */
    private static void validateLongitude(BigDecimal longitude) {
        if (longitude == null) {
            throw new IllegalArgumentException("경도는 필수입니다");
        }
        if (longitude.compareTo(MIN_LONGITUDE) < 0 || longitude.compareTo(MAX_LONGITUDE) > 0) {
            throw new IllegalArgumentException("경도는 -180 이상 180 이하여야 합니다");
        }
    }

    private Hub(String name, String address, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
