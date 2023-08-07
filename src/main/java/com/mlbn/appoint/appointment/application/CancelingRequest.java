package com.mlbn.appoint.appointment.application;

import com.mlbn.appoint.appointment.domain.AppointmentId;
import com.mlbn.appoint.appointment.domain.CancelCommand;
import com.mlbn.appoint.common.validation.Failure;
import io.vavr.control.Either;

import java.util.UUID;

record CancelingRequest(UUID appointmentId) {

    Either<Failure, CancelCommand> toCommand() {
        return CancelCommand.of(new AppointmentId(appointmentId));
    }
}
