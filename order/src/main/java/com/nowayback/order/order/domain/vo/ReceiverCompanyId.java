package com.nowayback.order.order.domain.vo;

import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReceiverCompanyId {

    private UUID id;

    private ReceiverCompanyId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        this.id = id;
    }

    public static ReceiverCompanyId of(UUID id) {
        return new ReceiverCompanyId(id);
    }
}
