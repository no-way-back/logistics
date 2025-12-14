package com.nowayback.common.event;

public interface EventType {
    EventType getType();
    Class<?> getPayloadClass();
    String getTopic();
}