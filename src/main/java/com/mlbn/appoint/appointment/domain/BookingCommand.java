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
public class BookingCommand {

    ClientId clientId;
    FacilityId facilityId;
    ProductId productId;
    EmployeeId employeeId;
    BookingSlot bookingSlot;
    Comment comment;

    public static Either<Failure, BookingCommand> of(ClientId clientId, FacilityId facilityId,
                                              ProductId productId, EmployeeId employeeId,
                                              BookingSlot bookingSlot, Comment comment, Clock clock) {
        return validate(clientId, facilityId, productId, employeeId, bookingSlot, comment, clock).toEither();
    }

    private static Validation<Failure, BookingCommand> validate(ClientId clientId, FacilityId facilityId,
                                                                ProductId productId, EmployeeId employeeId,
                                                                BookingSlot bookingSlot, Comment comment, Clock clock) {
        return Validator.validate(new BookingCommand(clientId, facilityId, productId, employeeId, bookingSlot, comment),
                ClientId.validate(clientId),
                FacilityId.validate(facilityId),
                ProductId.validate(productId),
                EmployeeId.validate(employeeId),
                BookingSlot.validate(bookingSlot, clock));
    }
}
