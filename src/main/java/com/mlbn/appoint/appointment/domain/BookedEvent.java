package com.mlbn.appoint.appointment.domain;

import com.mlbn.appoint.common.event.DomainEvent;
import lombok.Getter;

import java.time.Instant;

@Getter
class BookedEvent extends DomainEvent {

    private AppointmentId appointmentId;

    BookedEvent(Object source, AppointmentId appointmentId, Instant when) {
        super(source, when);
        this.appointmentId = appointmentId;
    }
}
