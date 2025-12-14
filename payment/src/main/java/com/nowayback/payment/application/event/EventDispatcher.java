package com.nowayback.payment.application.event;

import com.nowayback.common.event.Event;
import com.nowayback.common.event.EventHandler;
import com.nowayback.common.event.EventPayload;
import com.nowayback.payment.domain.ProcessedEventsRepository;
import com.nowayback.payment.domain.event.ProcessedEvents;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class EventDispatcher {

    private final List<EventHandler> eventHandlers;
    private final ProcessedEventsRepository processedEventsRepository;

    @Transactional
    public void dispatch(Event<? extends EventPayload> event) {
        if (!checkDuplicateAndSave(event)) return;

        eventHandlers.forEach(handler -> {
            if (handler.supports(event)) {
                handler.handle((Event<EventPayload>) event);
            }
        });
    }

    private boolean checkDuplicateAndSave(Event<?> event) {
        try {
            processedEventsRepository.save(
                ProcessedEvents.of(
                    event.getEventId(),
                    event.getType().toString()
                )
            );
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }
}
