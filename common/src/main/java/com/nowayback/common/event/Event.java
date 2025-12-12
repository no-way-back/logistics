package com.nowayback.common.event;

import com.nowayback.common.dataserializer.DataSerializer;
import java.util.UUID;

public class Event<T extends EventPayload> {

    protected UUID eventId;
    protected UUID aggregateId;
    protected EventType type;
    protected AggregateType aggregateType;
    protected T payload;

    public static Event<EventPayload> of(
        UUID eventId,
        UUID aggregateId,
        EventType eventType,
        AggregateType aggregateType,
        EventPayload eventPayload
    ) {
        Event<EventPayload> event = new Event<>();
        event.eventId = eventId;
        event.aggregateId = aggregateId;
        event.type = eventType;
        event.payload = eventPayload;
        event.aggregateType = aggregateType;
        return event;
    }

    public static <T extends EventPayload> Event<T> fromJson(
        String json,
        EventType eventType
    ) {
        EventRaw raw = DataSerializer.deserialize(json, EventRaw.class);
        if (raw == null) {
            return null;
        }

        Event<T> event = new Event<>();
        event.eventId = raw.getEventId();
        event.aggregateId = raw.getAggregateId();
        event.aggregateType = raw.getAggregateType();
        event.type = eventType;
        event.payload = DataSerializer.deserialize(
            raw.getPayload(),
            (Class<T>) eventType.getPayloadClass()
        );
        return event;
    }

    public static EventRaw toRaw(String json) {
        return DataSerializer.deserialize(json, EventRaw.class);
    }

    public static class EventRaw {
        private UUID eventId;
        private UUID aggregateId;
        private AggregateType aggregateType;
        private String type;
        private Object payload;

        public UUID getEventId() { return eventId; }
        public UUID getAggregateId() { return aggregateId; }
        public AggregateType getAggregateType() { return aggregateType; }
        public String getType() { return type; }
        public String getPayload() {
            return DataSerializer.serialize(payload);
        }
    }

    public UUID getEventId() { return eventId; }
    public UUID getAggregateId() { return aggregateId; }
    public EventType getType() { return type; }
    public AggregateType getAggregateType() { return aggregateType; }
    public T getPayload() { return payload; }
}
