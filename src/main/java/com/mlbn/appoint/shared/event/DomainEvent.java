package com.mlbn.appoint.shared.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;

@Getter
public class DomainEvent extends ApplicationEvent {

    private final AggregateId aggregateId;
    private final Instant when;

    public DomainEvent(Object source, AggregateId aggregateId, Instant when) {
        super(source);
        this.aggregateId = aggregateId;
        this.when = when;
    }
}

