package com.mlbn.appoint.domain.appointment;

import com.mlbn.appoint.shared.validation.Failure;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

import static com.mlbn.appoint.shared.validation.Results.*;

@RequiredArgsConstructor
public class CancelingService {

    private final Appointments repository;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Transactional
    public Either<Failure, Appointment> cancel(CancelCommand command) {
        return repository.findById(command.getAppointmentId())
            .map(appointment -> appointment.cancel()
                    .map(repository::save)
                    .map(this::publishEvent))
            .orElse(failure(String.format("Appointment not exist, id = %s", command.getAppointmentId().id())));
    }

    private Appointment publishEvent(Appointment appointment) {
        CanceledEvent event = new CanceledEvent(this,  appointment.getId(), Instant.now(clock));
        eventPublisher.publishEvent(event);
        return appointment;
    }
}
