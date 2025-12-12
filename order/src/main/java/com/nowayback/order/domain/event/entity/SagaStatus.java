package com.nowayback.order.domain.event.entity;

import com.nowayback.common.audit.BaseEntity;
import com.nowayback.order.domain.event.vo.SagaState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "saga_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SagaStatus extends BaseEntity {

    @Id
    @Column(name = "saga_id", nullable = false)
    private UUID sagaId;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 50)
    private SagaState state;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "max_retry", nullable = false)
    private int maxRetry;

    @Column(name = "last_event", length = 100)
    private String lastEvent;

    @Column(name = "compensation_step", length = 100)
    private String compensationStep;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public void updateState(SagaState newState) {
        this.state = newState;
    }

    public void incrementRetry(String error) {
        this.retryCount++;
        this.errorMessage = error;
    }

    public boolean canRetry() {
        return retryCount < maxRetry;
    }

    public void updateLastEvent(String event) {
        this.lastEvent = event;
    }

    public void updateCompensationStep(String step) {
        this.compensationStep = step;
    }

    public static SagaStatus create(UUID sagaId, UUID orderId) {
        return SagaStatus.builder()
            .sagaId(sagaId)
            .orderId(orderId)
            .state(SagaState.PENDING)
            .retryCount(0)
            .maxRetry(3)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }
}
