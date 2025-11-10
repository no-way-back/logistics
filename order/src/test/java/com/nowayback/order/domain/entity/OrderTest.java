package com.nowayback.order.domain.entity;

import static com.nowayback.order.fixture.OrderFixture.CUSTOMER_ID;
import static com.nowayback.order.fixture.OrderFixture.ORDER_ITEMS;
import static com.nowayback.order.fixture.OrderFixture.RECEIVER_COMPANY_ID;
import static com.nowayback.order.fixture.OrderFixture.RECEIVER_COMPANY_SNAPSHOT;
import static com.nowayback.order.fixture.OrderFixture.REQUEST;
import static com.nowayback.order.fixture.OrderFixture.SUPPLIER_COMPANY_ID;
import static com.nowayback.order.fixture.OrderFixture.SUPPLIER_COMPANY_SNAPSHOT;
import static com.nowayback.order.fixture.OrderFixture.createOrder;
import static com.nowayback.order.fixture.OrderFixture.createOrderWithStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.nowayback.order.domain.exception.OrderDomainException;
import com.nowayback.order.domain.policy.OrderStatusTransitionPolicy;
import com.nowayback.order.domain.vo.CustomerId;
import com.nowayback.order.domain.vo.OrderItems;
import com.nowayback.order.domain.vo.OrderStatus;
import com.nowayback.order.domain.vo.ProductId;
import com.nowayback.order.domain.vo.ReceiverCompanyId;
import com.nowayback.order.domain.vo.ReceiverCompanySnapshot;
import com.nowayback.order.domain.vo.SupplierCompanyId;
import com.nowayback.order.domain.vo.SupplierCompanySnapshot;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

class OrderTest {

    @Mock
    OrderStatusTransitionPolicy policy;

    @Test
    @DisplayName("모든 필드가 정상일 경우 주문 생성에 성공한다.")
    void createOrder_success() {
        // given
        CustomerId customerId = CUSTOMER_ID;
        SupplierCompanyId supplierId = SUPPLIER_COMPANY_ID;
        SupplierCompanySnapshot supplier = SUPPLIER_COMPANY_SNAPSHOT;
        ReceiverCompanyId receiverId = RECEIVER_COMPANY_ID;
        ReceiverCompanySnapshot receiver = RECEIVER_COMPANY_SNAPSHOT;
        OrderItems orderItems = ORDER_ITEMS;
        String request = REQUEST;

        // when
        Order order = Order.create(customerId, request, supplierId, supplier, receiverId, receiver, orderItems);

        // then
        assertThat(order.getRequest()).isEqualTo(request);
        assertThat(order.getSupplierCompanyId()).isEqualTo(supplierId);
        assertThat(order.getSupplierCompanySnapshot()).isEqualTo(supplier);
        assertThat(order.getReceiverCompanyId()).isEqualTo(receiverId);
        assertThat(order.getReceiverCompanySnapshot()).isEqualTo(receiver);
    }

    @Test
    @DisplayName("주문 생성 시 상태는 CREATE_PENDING 이어야 한다")
    void createOrder_ShouldHavePendingStatus() {
        // given
        // when
        Order order = Order.create(
            CUSTOMER_ID,
            REQUEST,
            SUPPLIER_COMPANY_ID,
            SUPPLIER_COMPANY_SNAPSHOT,
            RECEIVER_COMPANY_ID,
            RECEIVER_COMPANY_SNAPSHOT,
            ORDER_ITEMS
        );

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATE_PENDING);
    }

    @Test
    @DisplayName("주문 총 금액을 올바르게 설정한다")
    void createOrder_ShouldComputeTotalPrice() {
        // given
        OrderItems orderItems = OrderItems.of(
            List.of(
                OrderItem.create(ProductId.of(UUID.randomUUID()), "상품1", BigDecimal.valueOf(1000),
                    2))
        );
        // when
        Order order = Order.create(
            CUSTOMER_ID,
            REQUEST,
            SUPPLIER_COMPANY_ID,
            SUPPLIER_COMPANY_SNAPSHOT,
            RECEIVER_COMPANY_ID,
            RECEIVER_COMPANY_SNAPSHOT,
            orderItems
        );
        // then
        assertThat(order.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(1000 * 2));
    }

    @Test
    @DisplayName("주문 상품이 없으면 주문 생성에 실패한다.")
    void createOrder_ShouldPropagateOrderItemsValidationError() {
        // given
        OrderItems orderItems = OrderItems.of(List.of());

        // when / then
        assertThatThrownBy(() ->
            Order.create(
                CUSTOMER_ID,
                REQUEST,
                SUPPLIER_COMPANY_ID,
                SUPPLIER_COMPANY_SNAPSHOT,
                RECEIVER_COMPANY_ID,
                RECEIVER_COMPANY_SNAPSHOT,
                orderItems
            )
        ).isInstanceOf(OrderDomainException.class);
    }

    @Nested
    @DisplayName("주문 생성 필수 파라미터 null 검증")
    class OrderCreateNullValidation {

        @Test
        @DisplayName("공급업체 ID가 null이면 주문 생성에 실패한다")
        void createOrder_ShouldThrowWhenSupplierIdIsNull() {
            // when / then
            assertThatThrownBy(() ->
                Order.create(
                    CUSTOMER_ID,
                    REQUEST,
                    null,
                    SUPPLIER_COMPANY_SNAPSHOT,
                    RECEIVER_COMPANY_ID,
                    RECEIVER_COMPANY_SNAPSHOT,
                    ORDER_ITEMS
                )
            ).isInstanceOf(OrderDomainException.class);
        }

        @Test
        @DisplayName("공급업체가 null이면 주문 생성에 실패한다")
        void createOrder_ShouldThrowWhenSupplierSnapshotIsNull() {
            // when / then
            assertThatThrownBy(() ->
                Order.create(
                    CUSTOMER_ID,
                    REQUEST,
                    SUPPLIER_COMPANY_ID,
                    null,
                    RECEIVER_COMPANY_ID,
                    RECEIVER_COMPANY_SNAPSHOT,
                    ORDER_ITEMS
                )
            ).isInstanceOf(OrderDomainException.class);
        }

        @Test
        @DisplayName("수령업체 ID가 null이면 주문 생성에 실패한다")
        void createOrder_ShouldThrowWhenReceiverIdIsNull() {
            // when / then
            assertThatThrownBy(() ->
                Order.create(
                    CUSTOMER_ID,
                    REQUEST,
                    SUPPLIER_COMPANY_ID,
                    SUPPLIER_COMPANY_SNAPSHOT,
                    null,
                    RECEIVER_COMPANY_SNAPSHOT,
                    ORDER_ITEMS
                )
            ).isInstanceOf(OrderDomainException.class);
        }

        @Test
        @DisplayName("수령업체가 null이면 주문 생성에 실패한다")
        void createOrder_ShouldThrowWhenReceiverSnapshotIsNull() {
            // when / then
            assertThatThrownBy(() ->
                Order.create(
                    CUSTOMER_ID,
                    REQUEST,
                    SUPPLIER_COMPANY_ID,
                    SUPPLIER_COMPANY_SNAPSHOT,
                    RECEIVER_COMPANY_ID,
                    null,
                    ORDER_ITEMS
                )
            ).isInstanceOf(OrderDomainException.class);
        }
    }

    @Nested
    @DisplayName("주문 상태 변경 검증")
    class OrderStatusTransition {

        @Test
        @DisplayName("CreatePending 상태에서는 Created 상태로 변경할 수 있다.")
        void completeCreation_ShouldChangeStatusFromCreatePendingToCreated() {
            // given
            Order order = createOrderWithStatus(OrderStatus.CREATE_PENDING);

            // when
            order.completeCreation();

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
        }

        @ParameterizedTest
        @MethodSource("excludeCreatePending")
        @DisplayName("CreatePending이 아닌 상태에서는 Created 상태로 변경하면 예외가 발생한다.")
        void completeCreation_ShouldThrowWhenNotCreatedPending(OrderStatus status) {
            // given
            Order order = createOrderWithStatus(status);

            // when / then
            assertThatThrownBy(() -> {
                order.completeCreation();
            }).isInstanceOf(OrderDomainException.class);
        }

        static Stream<OrderStatus> excludeCreatePending() {
            return Stream.of(OrderStatus.values())
                .filter(status -> status != OrderStatus.CREATE_PENDING);
        }

        @ParameterizedTest
        @MethodSource("cancelAllowedStatus")
        @DisplayName("주문 취소는 CREATE_PENDING, CREATE에서 할 수 있다")
        void cancelOrder_ShouldOnlyHasCreatePendingAndCreated(OrderStatus status) {
            // given
            Order order = createOrderWithStatus(status);

            // when
            order.cancel(CUSTOMER_ID);

            //then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
        }

        static Stream<OrderStatus> cancelAllowedStatus() {
            return Stream.of(OrderStatus.CREATE_PENDING, OrderStatus.CREATED);
        }

        @ParameterizedTest
        @MethodSource("cancelNotAllowedStatus")
        @DisplayName("주문 취소가 불가능한 상태에서는 예외가 발생한다.")
        void cancelOrder_shouldThrowWhenDidNotCancelStatus(OrderStatus status) {
            // given
            Order order = createOrderWithStatus(status);

            // when / then
            assertThatThrownBy(() -> {
                order.cancel(CUSTOMER_ID);
            }).isInstanceOf(OrderDomainException.class);
        }

        static Stream<OrderStatus> cancelNotAllowedStatus() {
            return Stream.of(OrderStatus.DELIVERING, OrderStatus.COMPLETED, OrderStatus.CANCELED);
        }

        @Test
        @DisplayName("주문 취소시 CUSTOMER_ID가 다르면 예외가 발생한다.")
        void cancelOrder_whenNotOwner_shouldThrow() {
            // given
            Order order = createOrder();

            // when / then
            assertThatThrownBy(() -> {
                order.cancel(CustomerId.of(UUID.randomUUID()));
            }).isInstanceOf(OrderDomainException.class);
        }
    }
}