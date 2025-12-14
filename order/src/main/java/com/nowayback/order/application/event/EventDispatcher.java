package com.nowayback.order.application.event;

import com.nowayback.common.event.Event;
import com.nowayback.common.event.EventHandler;
import com.nowayback.common.event.EventPayload;
import com.nowayback.common.event.EventType;
import com.nowayback.order.domain.event.entity.ProcessedEvents;
import com.nowayback.order.domain.repository.ProcessedEventsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EventDispatcher {

    private final List<EventHandler> eventHandlers;
    private final ProcessedEventsRepository processedEventsRepository;

    @Transactional
    public void dispatch(Event<? extends EventPayload> event) {
        if (!tryMarkProcessed(event)) return;

        eventHandlers.forEach(handler -> {
            if (handler.supports(event)) {
                handler.handle((Event<EventPayload>) event);
            }
        });
    }

    private boolean tryMarkProcessed(Event<?> event) {
        int inserted = processedEventsRepository.insertIgnore(
            event.getEventId(),
            event.getType().getType().toString()
        );
        return inserted == 1;
    }
}
