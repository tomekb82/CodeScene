package com.mlbn.appoint.appointment.domain;

import com.mlbn.appoint.common.validation.Failure;
import com.mlbn.appoint.common.validation.Success;
import com.mlbn.appoint.common.vo.TimeSlot;
import com.mlbn.appoint.facility.domain.ProductId;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.mlbn.appoint.common.validation.Results.failure;
import static com.mlbn.appoint.common.validation.Results.success;

@RequiredArgsConstructor
public class SlotService implements Slots {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Appointments appointments;
    private final Products products;
    private final Clock clock;

    @Override
    public Either<Failure, Success> isAvailable(BookingCommand command) {
        Set<TimeSlot> openSlots = anOpenSlots(command.getProductId(), command.getBookingSlot());
        Set<BookingSlot> bookingSlots = aBookedSlots(command.getProductId(), command.getBookingSlot());
        if (isEmpty(openSlots)) {
            return failure("List of open openSlots should not be empty");
        }
        return isOpen(command.getBookingSlot(), openSlots) && isNotBooked(command.getBookingSlot(), bookingSlots)
                ? success()
                : failure("Product slot is not available");
    }

    @Override
    public Set<TimeSlot> findAvailable(ProductId productId, String date) {
        LocalDateTime parsedDate = LocalDate.parse(date, FORMATTER).atStartOfDay();
        BookingSlot bookingSlot = new BookingSlot(parsedDate, Duration.ZERO);
        Set<TimeSlot> openSlots = anOpenSlots(productId, bookingSlot);
        Set<BookingSlot> bookedSlots = aBookedSlots(productId, bookingSlot);
        return openSlots.stream()
                .filter(isNotBooked(bookedSlots).and(isInFuture(bookingSlot)))
                .collect(Collectors.toSet());
    }

    private Predicate<TimeSlot> isInFuture(BookingSlot bookingSlot) {
        return openSlot -> !aDateOfAppointment(bookingSlot).date().isEqual(LocalDate.now(clock))
                || openSlot.getStart().isAfter(LocalTime.now(clock));
    }

    private Predicate<TimeSlot> isNotBooked(Set<BookingSlot> bookedSlots) {
        return openSlot -> isEmpty(bookedSlots) || bookedSlots.stream().noneMatch(booked -> openSlot.isEqualTo(booked.startDate()));
    }

    private boolean isNotBooked(BookingSlot bookingSlot, Set<BookingSlot> bookedSlots) {
        return isEmpty(bookedSlots) || bookedSlots.stream().noneMatch(s -> s.isEqualTo(bookingSlot));
    }

    private boolean isOpen(BookingSlot bookingSlot, Set<TimeSlot> openSlots) {
        return openSlots.stream().anyMatch(s -> s.isEqualTo(bookingSlot.startDate()));
    }

    private Set<TimeSlot> anOpenSlots(ProductId productId, BookingSlot bookingSlot) {
        return products.findOpenSlotsForDayOfWeek(productId, aDateOfAppointment(bookingSlot).dayOfWeek());
    }

    private Set<BookingSlot> aBookedSlots(ProductId productId, BookingSlot bookingSlot) {
        return appointments.findBookedSlotsForDate(productId, aDateOfAppointment(bookingSlot));
    }

    private DateOfAppointment aDateOfAppointment(BookingSlot bookingSlot) {
        return DateOfAppointment.from(bookingSlot);
    }

    private static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }
}
