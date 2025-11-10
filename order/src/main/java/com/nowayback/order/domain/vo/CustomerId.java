package com.nowayback.order.domain.vo;

import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerId {
    private UUID id;

    private CustomerId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        this.id = id;
    }

    public static CustomerId of(UUID id) {
        return new CustomerId(id);
    }
}
