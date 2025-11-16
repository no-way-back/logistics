package com.nowayback.order.common.eventstore;

import com.nowayback.order.common.eventstore.vo.AggregateType;
import com.nowayback.order.common.eventstore.vo.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoredEventController {

    private final StoredEventRepository storedEventRepository;

    @GetMapping("/events")
    public Page<StoredEvent> getAllEvents(Pageable pageable) {
        return storedEventRepository.findAll(pageable);
    }

    @GetMapping(value = "/events", params = "eventType")
    public Page<StoredEvent> getEventsByType(
        @RequestParam("eventType") EventType eventType,
        Pageable pageable
    ) {
        return storedEventRepository.findByEventType(eventType, pageable);
    }

    @GetMapping(value = "/events", params = "aggregateType")
    public Page<StoredEvent> getEventsByAggregateType(
        @RequestParam("aggregateType") AggregateType aggregateType,
        Pageable pageable
    ) {
        return storedEventRepository.findByAggregateType(aggregateType, pageable);
    }
}
