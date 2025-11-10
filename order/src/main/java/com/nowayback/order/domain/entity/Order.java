package com.nowayback.order.domain.entity;

import static com.nowayback.order.domain.util.DomainPreconditions.notNull;

import com.nowayback.common.audit.BaseEntity;
import com.nowayback.order.domain.exception.OrderDomainErrorCode;
import com.nowayback.order.domain.policy.OrderStatusTransitionPolicy;
import com.nowayback.order.domain.vo.CustomerId;
import com.nowayback.order.domain.vo.OrderItems;
import com.nowayback.order.domain.vo.OrderStatus;
import com.nowayback.order.domain.vo.ReceiverCompanyId;
import com.nowayback.order.domain.vo.ReceiverCompanySnapshot;
import com.nowayback.order.domain.vo.SupplierCompanyId;
import com.nowayback.order.domain.vo.SupplierCompanySnapshot;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "customer_id"))
    private CustomerId customerId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "supplier_id"))
    private SupplierCompanyId supplierCompanyId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "receiver_id"))
    private ReceiverCompanyId receiverCompanyId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "request")
    private String request;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "supplier_company_name", nullable = false, updatable = false)),
        @AttributeOverride(name = "address", column = @Column(name = "supplier_company_address", nullable = false, updatable = false)),
        @AttributeOverride(name = "detailAddress", column = @Column(name = "supplier_company_detail_address", updatable = false)),
        @AttributeOverride(name = "contact", column = @Column(name = "supplier_company_contact", nullable = false, updatable = false))
    })
    private SupplierCompanySnapshot supplierCompanySnapshot;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "receiver_company_name", nullable = false, updatable = false)),
        @AttributeOverride(name = "address", column = @Column(name = "receiver_company_address", nullable = false, updatable = false)),
        @AttributeOverride(name = "detailAddress", column = @Column(name = "receiver_company_detail_address", updatable = false)),
        @AttributeOverride(name = "contact", column = @Column(name = "receiver_company_contact", nullable = false, updatable = false))
    })
    private ReceiverCompanySnapshot receiverCompanySnapshot;

    @Embedded
    private OrderItems orderItems;

    private Order(
        CustomerId customerId,
        SupplierCompanyId supplierCompanyId,
        SupplierCompanySnapshot supplierCompanySnapshot,
        ReceiverCompanyId receiverCompanyId,
        ReceiverCompanySnapshot receiverCompanySnapshot,
        OrderStatus status,
        BigDecimal totalPrice,
        String name,
        String request,
        OrderItems orderItems
    ) {
        this.customerId = customerId;
        this.supplierCompanyId = supplierCompanyId;
        this.supplierCompanySnapshot = supplierCompanySnapshot;
        this.receiverCompanyId = receiverCompanyId;
        this.receiverCompanySnapshot = receiverCompanySnapshot;
        this.status = status;
        this.totalPrice = totalPrice;
        this.name = name;
        this.request = request;
        this.orderItems = orderItems;
    }

    public static Order create(
        CustomerId customerId,
        String request,
        SupplierCompanyId supplierCompanyId,
        SupplierCompanySnapshot supplier,
        ReceiverCompanyId receiverCompanyId,
        ReceiverCompanySnapshot receiver,
        OrderItems orderItems) {

        validateSupplierAndReceiver(supplierCompanyId, supplier, receiverCompanyId, receiver);
        validateOrderItems(orderItems);

        return new Order(
            customerId,
            supplierCompanyId,
            supplier,
            receiverCompanyId,
            receiver,
            OrderStatus.CREATE_PENDING,
            orderItems.getTotalPrice(),
            orderItems.getOrderName(),
            request,
            orderItems
        );
    }

    public void completeCreation() {
        OrderStatusTransitionPolicy.assertCanTransition(this, OrderStatus.CREATED);

        this.status = OrderStatus.CREATED;
    }
    public void cancel() {
        OrderStatusTransitionPolicy.assertCanCancel(this, OrderStatus.CANCELED);

        this.status = OrderStatus.CANCELED;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems.asReadOnly();
    }

    private static void validateSupplierAndReceiver(
        SupplierCompanyId supplierCompanyId,
        SupplierCompanySnapshot supplier,
        ReceiverCompanyId receiverCompanyId,
        ReceiverCompanySnapshot receiver
    ) {
        notNull(supplierCompanyId, OrderDomainErrorCode.NULL_SUPPLIER_COMPANY_ID);
        notNull(supplier, OrderDomainErrorCode.NULL_SUPPLIER_COMPANY);
        notNull(receiverCompanyId, OrderDomainErrorCode.NULL_RECEIVER_COMPANY_ID);
        notNull(receiver, OrderDomainErrorCode.NULL_RECEIVER_COMPANY);
    }

    private static void validateOrderItems(OrderItems orderItems) {
        orderItems.validate();
    }


}
