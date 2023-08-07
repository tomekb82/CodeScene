package com.mlbn.appoint.appointment.infrastructure.jpa;

import com.mlbn.appoint.appointment.domain.*;
import com.mlbn.appoint.facility.domain.FacilityId;
import com.mlbn.appoint.facility.domain.ProductId;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AppointmentJpaRepository implements Appointments {

    private final AppointmentSpringRepository repository;
    private final AppointmentMapper mapper = Mappers.getMapper(AppointmentMapper.class);
    private final Clock clock;

    @Override
    public Optional<Appointment> findById(AppointmentId id) {
        return repository.findById(id.id()).map(toAppointment());
    }

    @Override
    public Appointment save(Appointment appointment) {
        return mapper.map(repository.save(mapper.map(appointment)), clock);
    }

    @Override
    public Set<BookingSlot> findBookedSlotsForDate(ProductId productId, DateOfAppointment dateOfAppointment) {
        return repository.findBookedSlotsForDate(productId.id(), dateOfAppointment.date()).stream()
                .map(toAppointment())
                .map(Appointment::getSlot)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Appointment> findByFacilityId(FacilityId facilityId) {
        return repository.findByFacilityId(facilityId.id())
                .stream()
                .map(toAppointment())
                .toList();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public List<Appointment> findAll() {
        return repository.findAll().stream()
                .map(toAppointment())
                .toList();
    }

    private Function<AppointmentEntity, Appointment> toAppointment() {
        return entity -> mapper.map(entity, clock);
    }

}
