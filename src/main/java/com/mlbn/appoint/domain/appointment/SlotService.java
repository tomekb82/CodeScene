package com.mlbn.appoint.domain.appointment;

import com.mlbn.appoint.shared.validation.Failure;
import com.mlbn.appoint.shared.validation.Success;
import com.mlbn.appoint.shared.vo.DateOfAppointment;
import com.mlbn.appoint.shared.vo.TimeSlot;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.mlbn.appoint.shared.validation.Results.*;

@RequiredArgsConstructor
public class SlotService {


    private final Appointments appointments;
    private final Products products;
    private final Clock clock;

    public Either<Failure, Success> isAvailable(BookingSlot bookingSlot) {
        Set<TimeSlot> openSlots = openSlots(bookingSlot);
        Set<BookingSlot> bookingSlots = bookedSlots(bookingSlot);
        if (openSlots.isEmpty()) {
            return failure("List of open slots should not be empty");
        }
        return isOpen(bookingSlot, openSlots) && isNotBooked(bookingSlot, bookingSlots)
                ? success()
                : failure("Product slot is not available");
    }

    public Set<TimeSlot> findAvailable(BookingSlot bookingSlot) {
        Set<TimeSlot> openSlots = openSlots(bookingSlot);
        Set<BookingSlot> bookedSlots = bookedSlots(bookingSlot);
        return openSlots.stream()
                .filter(isNotBooked(bookedSlots).and(isInFuture(bookingSlot)))
                .collect(Collectors.toSet());
    }

    private Predicate<TimeSlot> isInFuture(BookingSlot bookingSlot) {
        return openSlot -> !aDateOfAppointment(bookingSlot).date().isEqual(LocalDate.now(clock))
                || openSlot.getStart().isAfter(LocalTime.now(clock));
    }

    private Predicate<TimeSlot> isNotBooked(Set<BookingSlot> bookedSlots) {
        return openSlot -> bookedSlots.stream().noneMatch(booked -> openSlot.isEqualTo(booked.startDate()));
    }

    private boolean isNotBooked(BookingSlot bookingSlot, Set<BookingSlot> bookedSlots) {
        return bookedSlots.stream().noneMatch(s -> s.isEqualTo(bookingSlot));
    }

    private boolean isOpen(BookingSlot bookingSlot, Set<TimeSlot> openSlots) {
        return openSlots.stream().anyMatch(s -> s.isEqualTo(bookingSlot.startDate()));
    }

    private Set<TimeSlot> openSlots(BookingSlot bookingSlot) {
        return products.findOpenSlots(bookingSlot);
    }

    private Set<BookingSlot> bookedSlots(BookingSlot bookingSlot) {
        return appointments.findBookedSlots(bookingSlot);
    }

    private DateOfAppointment aDateOfAppointment(BookingSlot bookingSlot) {
        return DateOfAppointment.from(bookingSlot.startDate());
    }
}
