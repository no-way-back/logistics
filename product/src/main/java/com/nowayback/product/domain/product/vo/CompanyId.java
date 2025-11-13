package com.nowayback.product.domain.product.vo;

import com.nowayback.product.domain.product.exception.ProductDomainErrorCode;
import com.nowayback.product.domain.product.exception.ProductDomainException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyId {

    private UUID id;

    private CompanyId(UUID id) {
        this.id = id;
    }

    public static CompanyId of(UUID id) {
        if (id == null) {
            throw new ProductDomainException(ProductDomainErrorCode.NULL_COMPANY_ID_VALUE);
        }
        return new CompanyId(id);
    }
}
