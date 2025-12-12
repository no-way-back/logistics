package com.nowayback.common.event;

public interface EventType {
    String getType();
    Class<?> getPayloadClass();
    String getTopic();
}