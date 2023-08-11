package com.mlbn.appoint.infrastructure.facility.jpa;

import com.mlbn.appoint.domain.facility.ClosedSlot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
class ClosedSlotEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private LocalDate date;

    @OneToMany(cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.JOIN)
    private Set<TimeSlotEntity> slots;

    public ClosedSlotEntity(LocalDate date, Set<TimeSlotEntity> slots) {
        this.date = date;
        this.slots = slots;
    }

    public ClosedSlot toClosedSlot() {
        return new ClosedSlot(date,
                slots.stream().map(TimeSlotEntity::toTimeSlot).collect(Collectors.toSet()));
    }

    public static ClosedSlotEntity from(ClosedSlot closedSlot) {
        return new ClosedSlotEntity(closedSlot.date(),
                closedSlot.slots().stream().map(TimeSlotEntity::from).collect(Collectors.toSet()));
    }

    public static Set<ClosedSlotEntity> from(Set<ClosedSlot> configurationSlots) {
        return configurationSlots.stream()
                .map(ClosedSlotEntity::from)
                .collect(Collectors.toSet());
    }
}
