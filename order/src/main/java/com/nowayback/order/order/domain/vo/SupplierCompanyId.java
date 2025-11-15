package com.nowayback.order.order.domain.vo;

import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SupplierCompanyId {

    private UUID id;

    private SupplierCompanyId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    public static SupplierCompanyId of(UUID id) {
        return new SupplierCompanyId(id);
    }
}
