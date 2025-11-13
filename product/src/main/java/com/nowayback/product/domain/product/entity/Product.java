package com.nowayback.product.domain.product.entity;

import com.nowayback.common.audit.BaseEntity;
import com.nowayback.product.domain.product.exception.ProductDomainErrorCode;
import com.nowayback.product.domain.product.exception.ProductDomainException;
import com.nowayback.product.domain.product.vo.CompanyId;
import com.nowayback.product.domain.product.vo.HubId;
import com.nowayback.product.domain.product.vo.ProductInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id", updatable = false, nullable = false)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "supplier_id", nullable = false))
    private CompanyId supplierId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "hub_id", nullable = false))
    private HubId hubId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "name", nullable = false)),
            @AttributeOverride(name = "price", column = @Column(name = "price", nullable = false)),
    })
    private ProductInfo productInfo;

    public Product(CompanyId supplierId, HubId hubId, ProductInfo productInfo) {
        this.supplierId = supplierId;
        this.hubId = hubId;
        this.productInfo = productInfo;
    }

    public static Product create(CompanyId supplierId, HubId hubId, ProductInfo productInfo) {
        validateCompanyId(supplierId);
        validateHubId(hubId);
        validateProductInfo(productInfo);

        return new Product(supplierId, hubId, productInfo);
    }

    public void delete(UUID deletedBy) {
        softDelete(deletedBy);
    }

    private static void validateNotNull(Object object, ProductDomainErrorCode errorCode) {
        if (object == null) throw new ProductDomainException(errorCode);
    }

    public static void validateCompanyId(CompanyId supplierId) {
        validateNotNull(supplierId, ProductDomainErrorCode.NULL_SUPPLIER_ID_OBJECT);
    }

    public static void validateHubId(HubId hubId) {
        validateNotNull(hubId, ProductDomainErrorCode.NULL_HUB_ID_OBJECT);
    }

    public static void validateProductInfo(ProductInfo productInfo) {
        validateNotNull(productInfo, ProductDomainErrorCode.NULL_PRODUCT_INFO_OBJECT);
    }
}
