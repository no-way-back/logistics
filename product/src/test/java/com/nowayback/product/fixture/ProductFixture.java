package com.nowayback.product.fixture;

import com.nowayback.product.application.product.command.CreateProductCommand;
import com.nowayback.product.application.product.dto.ProductResult;
import com.nowayback.product.domain.product.entity.Product;
import com.nowayback.product.domain.product.vo.CompanyId;
import com.nowayback.product.domain.product.vo.HubId;
import com.nowayback.product.domain.product.vo.ProductInfo;
import com.nowayback.product.presentation.product.dto.request.CreateProductRequest;

import java.util.UUID;

public class ProductFixture {

    public static final UUID PRODUCT_ID = UUID.randomUUID();

    public static final UUID SUPPLIER_UUID = UUID.randomUUID();
    public static final UUID HUB_UUID = UUID.randomUUID();
    public static final String NAME = "Sample Product";
    public static final int PRICE = 12_000;
    public static final int QUANTITY = 0;

    public static final CompanyId SUPPLIER_ID = CompanyId.of(SUPPLIER_UUID);
    public static final HubId HUB_ID = HubId.of(HUB_UUID);
    public static final ProductInfo PRODUCT_INFO = ProductInfo.of(NAME, PRICE);

    /* product entity */
    public static Product createProduct() {
        return Product.create(
                SUPPLIER_ID,
                HUB_ID,
                PRODUCT_INFO
        );
    }

    /* product command */
    public static final CreateProductCommand CREATE_PRODUCT_COMMAND = CreateProductCommand.of(
            SUPPLIER_UUID,
            HUB_UUID,
            NAME,
            PRICE
    );

    /* product result */
    public static final ProductResult PRODUCT_RESULT = ProductResult.from(createProduct(), QUANTITY);

    /* product request */
    public static final CreateProductRequest VALID_CREATE_PRODUCT_REQUEST = new CreateProductRequest(
            SUPPLIER_UUID,
            HUB_UUID,
            NAME,
            PRICE
    );

    public static final CreateProductRequest INVALID_CREATE_PRODUCT_REQUEST = new CreateProductRequest(
            null,
            null,
            "",
            PRICE
    );
}
