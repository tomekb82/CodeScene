package com.mlbn.appoint.appointment.application;

import com.mlbn.appoint.appointment.domain.*;
import com.mlbn.appoint.common.validation.Failure;
import com.mlbn.appoint.facility.domain.EmployeeId;
import com.mlbn.appoint.facility.domain.FacilityId;
import com.mlbn.appoint.facility.domain.ProductId;
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
                new ProductId(productId),
                new EmployeeId(employeeId),
                new BookingSlot(LocalDateTime.parse(slotStartDate), Duration.parse(slotDuration)),
                new Comment(comment),
                clock);
    }

    static BookingRequest from(BookingCommand command) {
        return new BookingRequest(command.getClientId().id(),
                command.getFacilityId().id(),
                command.getProductId().id(),
                command.getEmployeeId().id(),
                command.getBookingSlot().startDate().toString(),
                command.getBookingSlot().duration().toString(),
                command.getComment().text());
    }
}
