package com.mlbn.appoint.infrastructure.appointment.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

interface AppointmentSpringRepository extends JpaRepository<AppointmentEntity, UUID> {

    @Query("select a from AppointmentEntity a " +
            "where a.productId = :productId " +
            "and a.status = 'CONFIRMED' " +
            "and CAST(a.slotStartDate AS date) = :date")
    List<AppointmentEntity> findBookedSlotsForDate(UUID productId, LocalDate date);

    List<AppointmentEntity> findByFacilityId(UUID facilityId);
}
