package com.mlbn.appoint.appointment.domain;

import com.mlbn.appoint.facility.domain.FacilityId;
import com.mlbn.appoint.facility.domain.ProductId;

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
    public Set<BookingSlot> findBookedSlotsForDate(ProductId productId, DateOfAppointment dateOfAppointment) {
        return db.values().stream()
                .filter(appointment -> appointment.getProductId().equals(productId))
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
