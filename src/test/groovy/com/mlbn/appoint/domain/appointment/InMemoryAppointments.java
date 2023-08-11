package com.mlbn.appoint.domain.appointment;

import com.mlbn.appoint.domain.appointment.Appointment;
import com.mlbn.appoint.domain.appointment.Appointments;
import com.mlbn.appoint.domain.facility.FacilityId;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class InMemoryAppointments implements Appointments {

    Map<UUID, Appointment> db = new ConcurrentHashMap<>();

    @Override
    public Optional<Appointment> findById(AppointmentId id) {
        return Optional.ofNullable(db.get(id.id()));
    }

    @Override
    public Appointment save(Appointment appointment) {
        db.put(appointment.getId().id(), appointment);
        return appointment;
    }

    @Override
    public Set<BookingSlot> findBookedSlots(BookingSlot bookingSlot) {
        return db.values().stream()
                .filter(appointment -> appointment.getSlot().productId().equals(bookingSlot.productId()))
                .filter(appointment -> appointment.getStatus().equals(Appointment.Status.CONFIRMED))
                .map(Appointment::getSlot)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Appointment> findAll() {
        return null;
    }

    @Override
    public List<Appointment> findByFacilityId(FacilityId facilityId) {
        return null;
    }
}
