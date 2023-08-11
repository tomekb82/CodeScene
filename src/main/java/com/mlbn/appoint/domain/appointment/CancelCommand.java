package com.mlbn.appoint.domain.appointment;

import com.mlbn.appoint.shared.validation.Failure;
import com.mlbn.appoint.shared.validation.Validator;
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
