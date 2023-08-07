package com.mlbn.appoint.appointment.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;

public record DateOfAppointment(LocalDate date) {

    public DayOfWeek dayOfWeek() {
        return date.getDayOfWeek();
    }

    public static DateOfAppointment from(BookingSlot bookingSlot) {
        return new DateOfAppointment(bookingSlot.startDate().toLocalDate());
    }
}
