package com.mlbn.appoint.domain.facility;

import com.mlbn.appoint.shared.vo.TimeSlot;

import java.time.LocalDate;
import java.util.Set;

public record ClosedSlot(LocalDate date, Set<TimeSlot> slots) {
}
