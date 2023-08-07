package com.mlbn.appoint.appointment.infrastructure.jpa;

import com.mlbn.appoint.appointment.domain.*;
import com.mlbn.appoint.facility.domain.EmployeeId;
import com.mlbn.appoint.facility.domain.FacilityId;
import com.mlbn.appoint.facility.domain.ProductId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Clock;
import java.time.Duration;
import java.util.Optional;
import java.util.logging.Logger;

@Mapper
interface AppointmentMapper {

    Logger logger = Logger.getLogger(AppointmentMapper.class.getName());

    @Mapping(target = "id", source = "id.id")
    @Mapping(target = "clientId", source = "clientId.id")
    @Mapping(target = "facilityId", source = "facilityId.id")
    @Mapping(target = "productId", source = "productId.id")
    @Mapping(target = "slotStartDate", source = "slot.startDate")
    @Mapping(target = "slotDuration", source = "slot.duration")
    @Mapping(target = "employeeId", source = "employeeId.id")
    @Mapping(target = "comment", source = "comment.text")
    @Mapping(target = "status", source = "status")
    AppointmentEntity map(Appointment appointment);

    default Appointment map(AppointmentEntity entity, Clock clock){
        AppointmentId appointmentId = new AppointmentId(entity.getId());
        Appointment.Status status = Optional.ofNullable(entity.getStatus())
                .map(s -> Appointment.Status.valueOf(s.name()))
                .orElse(null);
        ClientId clientId = new ClientId(entity.getClientId());
        FacilityId facilityId = new FacilityId(entity.getFacilityId());
        ProductId productId = new ProductId(entity.getProductId());
        EmployeeId employeeId = new EmployeeId(entity.getEmployeeId());
        BookingSlot serviceSlot = new BookingSlot(entity.getSlotStartDate(), Duration.parse(entity.getSlotDuration()));
        Comment comment = new Comment(entity.getComment());
        return FromExisitngCommand.of(appointmentId, status, clientId, facilityId, productId, employeeId, serviceSlot, comment, clock)
                .map(Appointment::fromExisting)
                .getOrElseThrow(() -> {
                    logger.info("Could not map appointment entity");
                    return new IllegalArgumentException("Could not map appointment entity");
                });
    }
}
