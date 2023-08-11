package com.mlbn.appoint.domain.appointment;

import com.mlbn.appoint.shared.validation.Failure;
import com.mlbn.appoint.shared.validation.Validator;
import com.mlbn.appoint.domain.facility.EmployeeId;
import com.mlbn.appoint.domain.facility.FacilityId;
import com.mlbn.appoint.shared.vo.ClientId;
import com.mlbn.appoint.shared.vo.Comment;
import io.vavr.control.Either;
import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.Clock;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FromExisitngCommand {
    AppointmentId appointmentId;
    Appointment.Status status;
    ClientId clientId;
    FacilityId facilityId;
    EmployeeId employeeId;
    BookingSlot serviceSlot;
    Comment comment;

    public static Either<Failure, FromExisitngCommand> of(AppointmentId appointmentId, Appointment.Status status,
                                                          ClientId clientId, FacilityId facilityId,
                                                          EmployeeId employeeId, BookingSlot slot,
                                                          Comment comment, Clock clock) {
        return validate(appointmentId, status, clientId, facilityId, employeeId, slot, comment, clock).toEither();
    }

    private static Validation<Failure, FromExisitngCommand> validate(AppointmentId appointmentId, Appointment.Status status,
                                                                     ClientId clientId, FacilityId facilityId,
                                                                     EmployeeId employeeId, BookingSlot slot,
                                                                     Comment comment, Clock clock) {
        return Validator.validate(new FromExisitngCommand(appointmentId, status, clientId, facilityId, employeeId, slot, comment),
                AppointmentId.validate(appointmentId),
                Appointment.Status.validate(status),
                ClientId.validate(clientId),
                FacilityId.validate(facilityId),
                EmployeeId.validate(employeeId),
                BookingSlot.validate(slot, clock));
    }




}
