package com.mlbn.appoint.domain.appointment;

import com.mlbn.appoint.shared.validation.Failure;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.util.UUID;

import static com.mlbn.appoint.shared.validation.Validator.isNotNull;

public record AppointmentId(UUID id) {

    private static final String NO_APPOINTMENT_ID_ERROR = "Appointment ID is required!";

    public static Validation<Seq<Failure>, AppointmentId> validate(AppointmentId appointmentId) {
        return Validation.combine(
                isNotNull(appointmentId, NO_APPOINTMENT_ID_ERROR),
                isNotNull(appointmentId, NO_APPOINTMENT_ID_ERROR).isValid()
                        ? isNotNull(appointmentId.id(), NO_APPOINTMENT_ID_ERROR)
                        : Validation.invalid(Failure.of(NO_APPOINTMENT_ID_ERROR))
        ).ap((id, ignore) -> id);
    }
}
