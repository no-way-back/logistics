package com.nowayback.order.domain.vo;

import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductId {
    private UUID id;

    private ProductId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        this.id = id;
    }

    public static ProductId of(UUID id) {
        return new ProductId(id);
    }
}
