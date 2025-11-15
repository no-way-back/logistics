package com.nowayback.order.order.domain.util;

import com.nowayback.common.exception.ErrorCode;
import com.nowayback.order.order.domain.exception.OrderDomainException;
import java.math.BigDecimal;
import java.util.Collection;

public final class DomainPreconditions {
    private DomainPreconditions() {}
    public static <T> T notNull(T value, ErrorCode code) {
        if (value == null) throw new OrderDomainException(code);
        return value;
    }

    public static String hasText(String value, ErrorCode code) {
        if (value == null || value.trim().isEmpty()) throw new OrderDomainException(code);
        return value;
    }

    public static long nonNegative(long value, ErrorCode code) {
        if (value < 0) throw new OrderDomainException(code);
        return value;
    }

    public static BigDecimal nonNegative(BigDecimal value, ErrorCode code) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderDomainException(code);
        }
        return value;
    }

    public static <T, C extends Collection<T>> C nonEmpty(C collection, ErrorCode code) {
        if (collection == null || collection.isEmpty()) {
            throw new OrderDomainException(code);
        }

        return collection;
    }
}
