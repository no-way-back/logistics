package com.nowayback.delivery.infrastructure.delivery;

import com.nowayback.delivery.domain.delivery.entity.Delivery;
import com.nowayback.delivery.domain.delivery.vo.DeliveryStatus;
import com.nowayback.delivery.domain.delivery.vo.HubId;
import com.nowayback.delivery.domain.delivery.vo.OrderId;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nowayback.delivery.domain.delivery.entity.QDelivery.delivery;

@Repository
@RequiredArgsConstructor
public class DeliveryCustomRepositoryImpl implements DeliveryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Delivery> searchDeliveries(OrderId orderId, HubId sourceHubId, HubId destinationHubId, DeliveryStatus status, int page, int size) {
        BooleanExpression condition = searchCondition(orderId, sourceHubId, destinationHubId, status);

        List<Delivery> deliveries = queryFactory
                .selectFrom(delivery)
                .where(condition)
                .orderBy(delivery.createdAt.desc())
                .offset((long) page * size)
                .limit(size)
                .fetch();

        JPAQuery<Long> total = queryFactory
                .select(delivery.count())
                .from(delivery)
                .where(condition);

        return PageableExecutionUtils.getPage(deliveries, PageRequest.of(page, size), total::fetchOne);
    }

    private BooleanExpression searchCondition(OrderId orderId, HubId sourceHubId, HubId destinationHubId, DeliveryStatus status) {
        return deletedAtIsNull()
                .and(orderIdEq(orderId))
                .and(sourceHubIdEq(sourceHubId))
                .and(destinationHubIdEq(destinationHubId))
                .and(statusEq(status));
    }

    private BooleanExpression deletedAtIsNull() {
        return delivery.deletedAt.isNull();
    }

    private BooleanExpression orderIdEq(OrderId orderId) {
        return valueEq(delivery.orderId, orderId);
    }

    private BooleanExpression sourceHubIdEq(HubId sourceHubId) {
        return valueEq(delivery.sourceHubId, sourceHubId);
    }

    private BooleanExpression destinationHubIdEq(HubId destinationHubId) {
        return valueEq(delivery.destinationHubId, destinationHubId);
    }

    private BooleanExpression statusEq(DeliveryStatus status) {
        return valueEq(delivery.status, status);
    }

    private <T> BooleanExpression valueEq(SimpleExpression<T> expression, T value) {
        if (value == null) return null;
        return expression.eq(value);
    }
}
