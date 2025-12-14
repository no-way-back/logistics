package com.nowayback.common.utils;

import java.util.function.Supplier;

public final class Retry {

    private static final int DEFAULT_MAX_RETRIES = 4;
    private static final long DEFAULT_INITIAL_DELAY_MS = 1000;

    private Retry() {
    }

    public static <T> T callWithRetry(Supplier<T> action) {
        return callWithRetry(action, DEFAULT_MAX_RETRIES, DEFAULT_INITIAL_DELAY_MS);
    }

    public static <T> T callWithRetry(
        Supplier<T> action,
        int maxRetries,
        long initialDelayMs
    ) {
        long delayMs = initialDelayMs;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return action.get();
            } catch (Exception e) {
                if (attempt == maxRetries) {
                    throw e;
                }
                sleepSafely(delayMs);
                delayMs *= 2;
            }
        }

        throw new IllegalStateException();
    }

    private static void sleepSafely(long ms) {
        if (ms <= 0) {
            return;
        }
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Retry interrupted", ie);
        }
    }
}
