package com.mlbn.appoint.facility.infrastructure.jpa;

import com.mlbn.appoint.facility.domain.ConfigurationSlot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.DayOfWeek;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
class ConfigurationSlotEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @OneToMany(cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.JOIN)
    private Set<TimeSlotEntity> openSlots;

    @OneToMany(cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.JOIN)
    private Set<TimeSlotEntity> closedSlots;

    public ConfigurationSlotEntity(DayOfWeek dayOfWeek, Set<TimeSlotEntity> openSlots, Set<TimeSlotEntity> closedSlots) {
        this.dayOfWeek = dayOfWeek;
        this.openSlots = openSlots;
        this.closedSlots = closedSlots;
    }

    public ConfigurationSlot toConfigurationSlot() {
        return new ConfigurationSlot(dayOfWeek,
                openSlots.stream().map(TimeSlotEntity::toTimeSlot).collect(Collectors.toSet()),
                closedSlots.stream().map(TimeSlotEntity::toTimeSlot).collect(Collectors.toSet()));
    }

    public static ConfigurationSlotEntity from(ConfigurationSlot configurationSlot) {
        return new ConfigurationSlotEntity(configurationSlot.dayOfWeek(),
                configurationSlot.openSlots().stream().map(TimeSlotEntity::from).collect(Collectors.toSet()),
                configurationSlot.closedSlots().stream().map(TimeSlotEntity::from).collect(Collectors.toSet()));
    }

    public static Set<ConfigurationSlotEntity> from(Set<ConfigurationSlot> configurationSlots) {
        return configurationSlots.stream()
                .map(ConfigurationSlotEntity::from)
                .collect(Collectors.toSet());
    }
}
