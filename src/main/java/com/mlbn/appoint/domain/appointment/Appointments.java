package com.mlbn.appoint.domain.appointment;

import com.mlbn.appoint.domain.facility.FacilityId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Appointments {

    Optional<Appointment> findById(AppointmentId id);

    Set<BookingSlot> findBookedSlots(BookingSlot bookingSlot);

    List<Appointment> findByFacilityId(FacilityId facilityId);

    Appointment save(Appointment appointment);

    void deleteAll();

    List<Appointment> findAll();

}
