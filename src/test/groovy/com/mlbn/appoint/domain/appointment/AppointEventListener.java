package com.mlbn.appoint.domain.appointment;

import com.mlbn.appoint.shared.event.DomainEvent;
import org.springframework.context.ApplicationListener;

import java.util.ArrayList;
import java.util.List;

public class AppointEventListener implements ApplicationListener<DomainEvent> {

    private final List<DomainEvent> events = new ArrayList<>();

    @Override
    public void onApplicationEvent(DomainEvent event) {
        events.add(event);
    }

    public List<DomainEvent> getEvents() {
        return events;
    }

    public void clear(){
        events.clear();
    }
}