package com.nowayback.payment.application.event;

import com.nowayback.common.event.Event;
import com.nowayback.common.event.EventHandler;
import com.nowayback.common.event.EventPayload;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventDispatcher {

    private final List<EventHandler> eventHandlers;

    public void dispatch(Event<? extends EventPayload> event) {
        eventHandlers.forEach(handler -> {
            if (handler.supports(event)) {
                handler.handle((Event<EventPayload>) event);
            }
        });
    }
}
