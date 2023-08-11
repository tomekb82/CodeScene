package com.mlbn.appoint.domain.appointment;

import com.mlbn.appoint.shared.event.AggregateId;
import com.mlbn.appoint.shared.event.DomainEvent;
import lombok.Getter;

import java.time.Instant;

@Getter
class BookedEvent extends DomainEvent {

    BookedEvent(Object source, AppointmentId appointmentId, Instant when) {
        super(source, new AggregateId(appointmentId.id()), when);
    }
}
