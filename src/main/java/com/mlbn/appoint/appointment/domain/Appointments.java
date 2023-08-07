package com.mlbn.appoint.appointment.domain;

import com.mlbn.appoint.facility.domain.FacilityId;
import com.mlbn.appoint.facility.domain.ProductId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Appointments {

    Optional<Appointment> findById(AppointmentId id);

    Set<BookingSlot> findBookedSlotsForDate(ProductId productId, DateOfAppointment dateOfAppointment);

    List<Appointment> findByFacilityId(FacilityId facilityId);

    Appointment save(Appointment appointment);

    void deleteAll();

    List<Appointment> findAll();

}
