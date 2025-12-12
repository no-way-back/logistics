package com.nowayback.order.application.event;

import com.nowayback.common.event.Event;
import com.nowayback.common.event.EventHandler;
import com.nowayback.common.event.EventPayload;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
