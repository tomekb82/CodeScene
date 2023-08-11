package com.mlbn.appoint.shared.vo;

import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class TimeSlot {

    LocalTime start;
    LocalTime end;
    Duration duration;

    private TimeSlot(LocalTime start, Duration duration) {
        if (start == null || duration == null) {
            throw new IllegalArgumentException("Start and end times are required.");
        }
        if (duration.isNegative() || duration.isZero()) {
            throw new IllegalArgumentException("Duration should be positive.");
        }
        this.start = start;
        this.end = start.plus(duration);
        this.duration = duration;
    }

    public static TimeSlot of(LocalTime start, Duration duration) {
        return new TimeSlot(start, duration);
    }

    public boolean isEqualTo(LocalDateTime input) {
        return start.truncatedTo(ChronoUnit.MINUTES).equals(input.toLocalTime().truncatedTo(ChronoUnit.MINUTES));
    }
}
