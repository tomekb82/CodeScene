package com.mlbn.appoint.common.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Getter
@EqualsAndHashCode
public class TimeSlot{

    LocalTime start;
    LocalTime end;
    Duration duration;

    private TimeSlot(LocalTime start, LocalTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end times are required.");
        }
        if (start.isAfter(end) || start.equals(end)) {
            throw new IllegalArgumentException("Start time should be before end time.");
        }
        this.start = start;
        this.end = end;
        this.duration = Duration.between(start, end);
    }

    private TimeSlot(LocalTime start, Duration duration) {
        if (start == null || duration == null) {
            throw new IllegalArgumentException("Start and end times are required.");
        }
        if (duration.isNegative()) {
            throw new IllegalArgumentException("Duration should be positive.");
        }
        this.start = start;
        this.end = start.plus(duration);
        this.duration = duration;
    }

    public static TimeSlot of(LocalTime start, LocalTime end) {
        return new TimeSlot(start, end);
    }

    public static TimeSlot of(LocalTime start, Duration duration) {
        return new TimeSlot(start, duration);
    }

    public boolean isEqualTo(LocalDateTime input) {
        return start.truncatedTo(ChronoUnit.MINUTES).equals(input.toLocalTime().truncatedTo(ChronoUnit.MINUTES));
    }
}
