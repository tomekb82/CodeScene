package com.mlbn.appoint.common.event;

import org.springframework.context.ApplicationEvent;

import java.time.Instant;

public class DomainEvent extends ApplicationEvent {

    public DomainEvent(Object source, Instant when) {
        super(source);
    }
}

