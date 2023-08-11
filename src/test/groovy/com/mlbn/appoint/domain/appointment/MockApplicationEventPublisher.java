package com.mlbn.appoint.domain.appointment;

import com.mlbn.appoint.shared.event.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;

class MockApplicationEventPublisher implements ApplicationEventPublisher {

    private final List<DomainEvent> publishedEvents = new ArrayList<>();

    @Override
    public void publishEvent(Object event) {
        publishedEvents.add((DomainEvent) event);
    }

    public List<DomainEvent> events() {
        return publishedEvents;
    }
}
