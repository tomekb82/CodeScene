package com.mlbn.appoint.domain.appointment;

import com.mlbn.appoint.shared.event.AggregateId;
import com.mlbn.appoint.shared.event.DomainEvent;

import java.time.Instant;

class CanceledEvent extends DomainEvent {

    CanceledEvent(Object source, AppointmentId appointmentId, Instant when) {
        super(source, new AggregateId(appointmentId.id()), when);
    }
}
