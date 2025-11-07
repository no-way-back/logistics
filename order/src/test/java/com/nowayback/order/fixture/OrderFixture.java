package com.nowayback.order.fixture;

import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.domain.entity.OrderItem;
import com.nowayback.order.domain.vo.OrderItems;
import com.nowayback.order.domain.vo.OrderStatus;
import com.nowayback.order.domain.vo.ProductId;
import com.nowayback.order.domain.vo.ReceiverCompanyId;
import com.nowayback.order.domain.vo.ReceiverCompanySnapshot;
import com.nowayback.order.domain.vo.SupplierCompanyId;
import com.nowayback.order.domain.vo.SupplierCompanySnapshot;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class OrderFixture {

    public static final SupplierCompanyId SUPPLIER_COMPANY_ID = SupplierCompanyId.of(UUID.randomUUID());
    public static final SupplierCompanySnapshot SUPPLIER_COMPANY_SNAPSHOT = SupplierCompanySnapshot.of(
        "공급업체", "서울특별시", "동작구", "slack_1234"
    );
    public static final ReceiverCompanyId RECEIVER_COMPANY_ID = ReceiverCompanyId.of(UUID.randomUUID());
    public static final ReceiverCompanySnapshot RECEIVER_COMPANY_SNAPSHOT = ReceiverCompanySnapshot.of(
        "수령업체", "서울특별시", "동작구", "slack_1234"
    );

    public static final OrderItems ORDER_ITEMS = OrderItems.of(
        List.of(OrderItem.create(ProductId.of(UUID.randomUUID()), "상품1", BigDecimal.valueOf(1000), 2))
    );

    public static final String REQUEST = "요청사항";

    public static Order createOrder() {
        return Order.create(
            REQUEST,
            SUPPLIER_COMPANY_ID,
            SUPPLIER_COMPANY_SNAPSHOT,
            RECEIVER_COMPANY_ID,
            RECEIVER_COMPANY_SNAPSHOT,
            ORDER_ITEMS
        );
    }

    public static Order createOrderWithStatus(OrderStatus status) {
        Order order = createOrder();
        setPrivateField(order, "status", status);
        return order;
    }

    public static Order createOrderWithNullField(String fieldName) {
        Order order = createOrder();
        setPrivateField(order, fieldName, null);
        return order;
    }

    private static void setPrivateField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
