package com.mlbn.appoint.facility.infrastructure.jpa;

import com.mlbn.appoint.common.vo.TimeSlot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
class TimeSlotEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private LocalTime startDate;

    @Column(nullable = false)
    private String duration;

    public TimeSlotEntity(LocalTime startDate, String duration) {
        this.startDate = startDate;
        this.duration = duration;
    }

    public static TimeSlotEntity from(TimeSlot slot) {
        return new TimeSlotEntity(slot.getStart(), slot.getDuration().toString());
    }

    public TimeSlot toTimeSlot() {
        return TimeSlot.of(startDate, Duration.parse(duration));
    }
}
