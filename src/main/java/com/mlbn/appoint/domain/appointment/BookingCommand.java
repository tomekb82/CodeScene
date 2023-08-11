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
public class BookingCommand {

    ClientId clientId;
    FacilityId facilityId;
    EmployeeId employeeId;
    BookingSlot bookingSlot;
    Comment comment;

    public static Either<Failure, BookingCommand> of(ClientId clientId, FacilityId facilityId, EmployeeId employeeId,
                                                     BookingSlot bookingSlot, Comment comment, Clock clock) {
        return validate(clientId, facilityId, employeeId, bookingSlot, comment, clock).toEither();
    }

    private static Validation<Failure, BookingCommand> validate(ClientId clientId, FacilityId facilityId,
                                                                EmployeeId employeeId, BookingSlot bookingSlot,
                                                                Comment comment, Clock clock) {
        return Validator.validate(new BookingCommand(clientId, facilityId, employeeId, bookingSlot, comment),
                ClientId.validate(clientId),
                FacilityId.validate(facilityId),
                EmployeeId.validate(employeeId),
                BookingSlot.validate(bookingSlot, clock));
    }
}
