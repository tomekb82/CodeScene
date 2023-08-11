package com.mlbn.appoint.shared

import com.mlbn.appoint.shared.vo.TimeSlot
import spock.lang.Specification

import java.time.Duration
import java.time.LocalTime

import static java.time.temporal.ChronoUnit.MINUTES
import static org.assertj.core.api.Assertions.assertThatExceptionOfType

class TimeSlotTest extends Specification {

    static LocalTime NOON = LocalTime.of(12, 12, 10)
    static LocalTime NOON_FIVE = NOON.plus(5, MINUTES)
    static final LocalTime NOON_TEN = NOON_FIVE.plus(5, MINUTES)

     def 'start must be before end'() {
        expect:
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TimeSlot.of(NOON_FIVE, Duration.between(NOON_FIVE, NOON)))
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TimeSlot.of(NOON_TEN, Duration.between(NOON_TEN, NOON)))
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TimeSlot.of(NOON_TEN, Duration.between(NOON_TEN, NOON_FIVE)))
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TimeSlot.of(NOON_TEN, Duration.between(NOON_TEN, NOON_TEN)))
    }

    def 'can create valid slot'() {
        given:
        TimeSlot noonToFive = TimeSlot.of(NOON, Duration.between(NOON, NOON_FIVE))
        TimeSlot fiveToTen = TimeSlot.of(NOON_FIVE, Duration.between(NOON_FIVE, NOON_TEN))

        expect:
        NOON == noonToFive.getStart()
        NOON_FIVE == noonToFive.getEnd()
        NOON_FIVE == fiveToTen.getStart()
        NOON_TEN == fiveToTen.getEnd()
    }
}
