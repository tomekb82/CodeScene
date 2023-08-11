package com.mlbn.appoint.shared.vo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record DateOfAppointment(LocalDate date) {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DayOfWeek dayOfWeek() {
        return date.getDayOfWeek();
    }

    public static DateOfAppointment from(LocalDateTime date) {
        return new DateOfAppointment(date.toLocalDate());
    }

    public static DateOfAppointment from(String date){
        return new DateOfAppointment(LocalDate.parse(date, FORMATTER));
    }
}
