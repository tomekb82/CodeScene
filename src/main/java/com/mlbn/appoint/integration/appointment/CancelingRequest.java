package com.mlbn.appoint.integration.appointment;

import com.mlbn.appoint.domain.appointment.AppointmentId;
import com.mlbn.appoint.domain.appointment.CancelCommand;
import com.mlbn.appoint.shared.validation.Failure;
import io.vavr.control.Either;

import java.util.UUID;

record CancelingRequest(UUID appointmentId) {

    Either<Failure, CancelCommand> toCommand() {
        return CancelCommand.of(new AppointmentId(appointmentId));
    }
}
