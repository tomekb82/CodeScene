package com.mlbn.appoint.facility.domain;

import com.mlbn.appoint.common.vo.TimeSlot;

import java.time.DayOfWeek;
import java.util.Set;

public record ConfigurationSlot(DayOfWeek dayOfWeek, Set<TimeSlot> openSlots, Set<TimeSlot> closedSlots) {

    public boolean isSameDay(DayOfWeek dayOfWeek) {
        return this.dayOfWeek == dayOfWeek;
    }
}
