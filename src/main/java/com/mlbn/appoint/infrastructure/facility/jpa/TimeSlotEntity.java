package com.mlbn.appoint.infrastructure.facility.jpa;

import com.mlbn.appoint.shared.vo.TimeSlot;
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
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private String duration;

    public TimeSlotEntity(LocalTime start, LocalTime end, String duration) {
        this.startTime = start;
        this.endTime = end;
        this.duration = duration;
    }

    public static TimeSlotEntity from(TimeSlot slot) {
        return new TimeSlotEntity(slot.getStart(), slot.getEnd(), slot.getDuration().toString());
    }

    public TimeSlot toTimeSlot() {
        return TimeSlot.of(startTime, Duration.parse(duration));
    }
}
