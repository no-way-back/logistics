package com.nowayback.order.domain.event.entity;

import com.nowayback.common.audit.BaseEntity;
import com.nowayback.order.domain.event.vo.SagaState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column(name = "saga_id")
    private UUID sagaId;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private SagaState state;

    @Column(name = "last_event")
    private String lastEvent;

    @Column(name = "error_message")
    private String errorMessage;

    public void updateState(SagaState newState) {
        this.state = newState;
    }

    public static SagaStatus create(UUID sagaId, UUID orderId) {
        return SagaStatus.builder()
            .sagaId(sagaId)
            .orderId(orderId)
            .state(SagaState.STOCK_REQUESTED)
            .build();
    }
}
