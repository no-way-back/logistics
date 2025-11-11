package com.nowayback.order.infrastructure.persistence;

import static com.nowayback.order.domain.entity.QOrder.order;

import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.domain.vo.CustomerId;
import com.nowayback.order.domain.vo.OrderStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<Order> searchOrders(
        CustomerId customerId,
        OrderStatus status,
        String sort,
        String orderBy,
        PageRequest pageRequest
    ) {
        BooleanExpression condition = searchCondition(customerId, status);

        List<Order> orders = queryFactory
            .selectFrom(order)
            .where(condition)
            .orderBy(getOrderSpecifier(sort, orderBy))
            .offset(pageRequest.getOffset())
            .limit(pageRequest.getPageSize())
            .fetch();

        JPAQuery<Long> total = queryFactory
            .select(order.count())
            .from(order)
            .where(condition);

        return PageableExecutionUtils.getPage(
            orders,
            pageRequest,
            total::fetchOne
        );
    }

    private BooleanExpression searchCondition(CustomerId customerId, OrderStatus status) {
        return deletedAtIsNull()
            .and(customerIdEq(customerId))
            .and(statusEq(status));
    }

    private BooleanExpression deletedAtIsNull() {
        return order.deletedAt.isNull();
    }

    private BooleanExpression customerIdEq(CustomerId customerId) {
        return valueEq(order.customerId, customerId);
    }

    private BooleanExpression statusEq(OrderStatus status) {
        return valueEq(order.status, status);
    }

    private <T> BooleanExpression valueEq(SimpleExpression<T> expression, T value) {
        if (value == null) return null;
        return expression.eq(value);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sort, String orderBy) {
        boolean isAsc = "asc".equalsIgnoreCase(sort);

        if ("updatedAt".equals(orderBy)) {
            return isAsc ? order.updatedAt.asc() : order.updatedAt.desc();
        }

        return isAsc ? order.createdAt.asc() : order.createdAt.desc();
    }
}
