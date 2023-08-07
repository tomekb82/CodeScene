package com.mlbn.appoint.appointment.domain;

import com.mlbn.appoint.common.event.DomainEvent;
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
