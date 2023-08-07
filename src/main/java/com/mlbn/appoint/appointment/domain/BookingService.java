package com.mlbn.appoint.appointment.domain;

import com.mlbn.appoint.common.validation.Failure;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@RequiredArgsConstructor
public class BookingService {

    private final Appointments appointments;
    private final SlotService slotService;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Transactional
    public Either<Failure, Appointment> book(BookingCommand command) {
        return slotService.isAvailable(command)
                .flatMap(success -> Appointment.book(command))
                .map(appointments::save)
                .map(this::publishEvent);
    }

    private Appointment publishEvent(Appointment appointment) {
        BookedEvent event = new BookedEvent(this,  appointment.getId(), Instant.now(clock));
        eventPublisher.publishEvent(event);
        return appointment;
    }
}
