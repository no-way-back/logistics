package com.nowayback.product.application.product.command;

import com.nowayback.product.domain.product.vo.CompanyId;
import com.nowayback.product.domain.product.vo.HubId;
import com.nowayback.product.domain.product.vo.ProductInfo;

import java.util.UUID;

public record CreateProductCommand(
        CompanyId supplierId,
        HubId hubId,
        ProductInfo productInfo
) {

    public static CreateProductCommand of(
            UUID supplierId,
            UUID hubId,
            String name,
            int price
    ) {
        return new CreateProductCommand(
                CompanyId.of(supplierId),
                HubId.of(hubId),
                ProductInfo.of(name, price)
        );
    }
}
