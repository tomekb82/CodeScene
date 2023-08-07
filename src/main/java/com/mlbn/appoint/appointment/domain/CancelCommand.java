package com.mlbn.appoint.appointment.domain;

import com.mlbn.appoint.common.validation.Failure;
import com.mlbn.appoint.common.validation.Validator;
import io.vavr.control.Either;
import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CancelCommand {

    AppointmentId appointmentId;

    public static Either<Failure, CancelCommand> of(AppointmentId appointmentId) {
        return validate(appointmentId).toEither();
    }

    private static Validation<Failure, CancelCommand> validate(AppointmentId appointmentId) {
        return Validator.validate(new CancelCommand(appointmentId), AppointmentId.validate(appointmentId));
    }
}
