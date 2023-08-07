package com.mlbn.appoint.appointment.domain;

import com.mlbn.appoint.common.validation.Failure;
import com.mlbn.appoint.common.validation.Validator;
import com.mlbn.appoint.facility.domain.EmployeeId;
import com.mlbn.appoint.facility.domain.FacilityId;
import com.mlbn.appoint.facility.domain.ProductId;
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
    ProductId productId;
    EmployeeId employeeId;
    BookingSlot serviceSlot;
    Comment comment;

    public static Either<Failure, FromExisitngCommand> of(AppointmentId appointmentId, Appointment.Status status, ClientId clientId, FacilityId facilityId,
                                                          ProductId productId, EmployeeId employeeId,
                                                          BookingSlot slot, Comment comment, Clock clock) {
        return validate(appointmentId, status, clientId, facilityId, productId, employeeId, slot, comment, clock).toEither();
    }

    private static Validation<Failure, FromExisitngCommand> validate(AppointmentId appointmentId, Appointment.Status status,
                                                                     ClientId clientId, FacilityId facilityId,
                                                                     ProductId productId, EmployeeId employeeId,
                                                                     BookingSlot slot, Comment comment, Clock clock) {
        return Validator.validate(new FromExisitngCommand(appointmentId, status, clientId, facilityId, productId, employeeId, slot, comment),
                AppointmentId.validate(appointmentId),
                Appointment.Status.validate(status),
                ClientId.validate(clientId),
                FacilityId.validate(facilityId),
                ProductId.validate(productId),
                EmployeeId.validate(employeeId),
                BookingSlot.validate(slot, clock));
    }




}
