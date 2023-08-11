package com.mlbn.appoint.domain.facility;

import com.mlbn.appoint.shared.vo.TimeSlot;

import java.time.DayOfWeek;
import java.util.Set;

public record ConfigurationSlot(DayOfWeek dayOfWeek, Set<TimeSlot> slots) {

    public boolean isSameDay(DayOfWeek dayOfWeek) {
        return this.dayOfWeek == dayOfWeek;
    }
}
