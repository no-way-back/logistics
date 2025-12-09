package com.nowayback.payment.domain.entity;

import com.nowayback.common.audit.BaseEntity;
import com.nowayback.payment.domain.vo.PaymentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "order_id", updatable = false, nullable = false)
    private UUID orderId;

    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    @Column(name = "amount", updatable = false, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "pg_payment_key")
    private String pgPaymentKey;

    @Column(name = "pg_order_id")
    private String pgOrderId;

    @Column(name = "pg_method")
    private String pgMethod;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "failed_at")
    private LocalDateTime failedAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    private Payment(UUID orderId, UUID userId, BigDecimal amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    public static Payment create(UUID orderId, UUID userId, BigDecimal amount) {
        return new Payment(orderId, userId, amount);
    }

    public void confirm(String pgPaymentKey, String pgOrderId, String pgMethod, LocalDateTime approvedAt) {
        this.pgPaymentKey = pgPaymentKey;
        this.pgOrderId = pgOrderId;
        this.pgMethod = pgMethod;
        this.approvedAt = approvedAt;
        this.status = PaymentStatus.COMPLETED;
    }

    public void fail() {
        this.failedAt = LocalDateTime.now();
        this.status = PaymentStatus.FAILED;
    }

    public void cancel(String cancelReason, LocalDateTime canceledAt) {
        this.cancelReason = cancelReason;
        this.canceledAt = canceledAt;
        this.status = PaymentStatus.CANCELED;
    }
}
