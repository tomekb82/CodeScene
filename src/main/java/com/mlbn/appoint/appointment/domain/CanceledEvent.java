package com.mlbn.appoint.appointment.domain;

import com.mlbn.appoint.common.event.DomainEvent;

import java.time.Instant;

class CanceledEvent extends DomainEvent {

    private final AppointmentId appointmentId;

    CanceledEvent(Object source, AppointmentId appointmentId, Instant when) {
        super(source, when);
        this.appointmentId = appointmentId;
    }
}
