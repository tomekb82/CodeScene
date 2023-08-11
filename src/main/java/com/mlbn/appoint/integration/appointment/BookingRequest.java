package com.mlbn.appoint.integration.appointment;

import com.mlbn.appoint.domain.appointment.BookingSlot;
import com.mlbn.appoint.shared.vo.ClientId;
import com.mlbn.appoint.shared.vo.Comment;
import com.mlbn.appoint.shared.validation.Failure;
import com.mlbn.appoint.domain.appointment.BookingCommand;
import com.mlbn.appoint.domain.facility.EmployeeId;
import com.mlbn.appoint.domain.facility.FacilityId;
import com.mlbn.appoint.domain.facility.ProductId;
import io.vavr.control.Either;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

record BookingRequest(UUID clientId, UUID facilityId, UUID productId, UUID employeeId,
                      String slotStartDate, String slotDuration, String comment) {

    Either<Failure, BookingCommand> toCommand(Clock clock) {
        return BookingCommand
                .of(
                new ClientId(clientId),
                new FacilityId(facilityId),
                new EmployeeId(employeeId),
                new BookingSlot(new ProductId(productId), LocalDateTime.parse(slotStartDate), Duration.parse(slotDuration)),
                new Comment(comment),
                clock);
    }

    static BookingRequest from(BookingCommand command) {
        return new BookingRequest(command.getClientId().id(),
                command.getFacilityId().id(),
                command.getBookingSlot().productId().id(),
                command.getEmployeeId().id(),
                command.getBookingSlot().startDate().toString(),
                command.getBookingSlot().duration().toString(),
                command.getComment().text());
    }
}
