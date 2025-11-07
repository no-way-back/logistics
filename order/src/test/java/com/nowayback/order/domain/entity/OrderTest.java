package com.nowayback.order.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import com.nowayback.order.domain.vo.OrderItems;
import com.nowayback.order.domain.vo.OrderStatus;
import com.nowayback.order.domain.vo.ReceiverCompanyId;
import com.nowayback.order.domain.vo.ReceiverCompanySnapshot;
import com.nowayback.order.domain.vo.SupplierCompanyId;
import com.nowayback.order.domain.vo.SupplierCompanySnapshot;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    private OrderItems orderItems;
    @Mock
    private SupplierCompanySnapshot supplierSnapshot;
    @Mock
    private ReceiverCompanySnapshot receiverSnapshot;

    private SupplierCompanyId anySupplierId() {
        return SupplierCompanyId.of(UUID.randomUUID());
    }

    private ReceiverCompanyId anyReceiverId() {
        return ReceiverCompanyId.of(UUID.randomUUID());
    }

    @Test
    @DisplayName("주문 생성 시 상태는 CREATE_PENDING 이어야 한다")
    void createOrder_ShouldHavePendingStatus() {
        // given
        SupplierCompanyId supplierId = anySupplierId();
        ReceiverCompanyId receiverId = anyReceiverId();

        BigDecimal totalPrice = new BigDecimal("2000");
        given(orderItems.getTotalPrice()).willReturn(totalPrice);

        // when
        Order order = Order.create(
            "요청사항",
            supplierId,
            supplierSnapshot,
            receiverId,
            receiverSnapshot,
            orderItems
        );

        // then
        assertThat(order).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATE_PENDING);
    }

    @Test
    @DisplayName("주문 총 금액을 올바르게 설정한다")
    void createOrder_ShouldComputeTotalPrice() {
        // given
        SupplierCompanyId supplierId = anySupplierId();
        ReceiverCompanyId receiverId = anyReceiverId();
        BigDecimal totalPrice = new BigDecimal("123456");

        given(orderItems.getTotalPrice()).willReturn(totalPrice);

        // when
        Order order = Order.create(
            "문 앞에 놓아주세요",
            supplierId,
            supplierSnapshot,
            receiverId,
            receiverSnapshot,
            orderItems
        );

        // then
        assertThat(order.getTotalPrice()).isEqualByComparingTo(totalPrice);
    }

    @Test
    @DisplayName("주문 생성시 거래처 ID 및 스냅샷이 그대로 세팅된다")
    void createOrder_ShouldEmbedSupplierAndReceiverSnapshots() {
        // given
        SupplierCompanyId supplierId = anySupplierId();
        ReceiverCompanyId receiverId = anyReceiverId();

        given(orderItems.getTotalPrice()).willReturn(new BigDecimal("1000"));

        // when
        Order order = Order.create(
            null,
            supplierId,
            supplierSnapshot,
            receiverId,
            receiverSnapshot,
            orderItems
        );

        // then
        assertThat(order.getSupplierCompanyId()).isEqualTo(supplierId);
        assertThat(order.getReceiverCompanyId()).isEqualTo(receiverId);
        assertThat(order.getSupplierCompanySnapshot()).isSameAs(supplierSnapshot);
        assertThat(order.getReceiverCompanySnapshot()).isSameAs(receiverSnapshot);
    }

    @Test
    @DisplayName("주문 생성시 요청사항을 저장한다")
    void createOrder_ShouldStoreRequest() {
        // given
        SupplierCompanyId supplierId = anySupplierId();
        ReceiverCompanyId receiverId = anyReceiverId();

        String req = "요청사항";

        // when
        Order order = Order.create(
            req,
            supplierId,
            supplierSnapshot,
            receiverId,
            receiverSnapshot,
            orderItems
        );

        // then
        assertThat(order.getRequest()).isEqualTo(req);
    }

    @Test
    @DisplayName("OrderItems.validate 에서 예외가 발생하면 주문 생성에 실패한다")
    void createOrder_ShouldPropagateOrderItemsValidationError() {
        // given
        SupplierCompanyId supplierId = anySupplierId();
        ReceiverCompanyId receiverId = anyReceiverId();

        RuntimeException exception = new RuntimeException();
        willThrow(exception).given(orderItems).validate();

        // when / then
        assertThatThrownBy(() ->
            Order.create(
                "req",
                supplierId,
                supplierSnapshot,
                receiverId,
                receiverSnapshot,
                orderItems
            )
        ).isSameAs(exception);
    }
}
