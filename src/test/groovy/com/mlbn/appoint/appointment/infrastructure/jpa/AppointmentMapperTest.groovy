package com.mlbn.appoint.appointment.infrastructure.jpa

import com.mlbn.appoint.appointment.domain.Appointment
import com.mlbn.appoint.appointment.domain.FromExisitngCommand
import org.junit.jupiter.api.Assertions
import org.mapstruct.factory.Mappers
import spock.lang.Specification

import static com.mlbn.appoint.common.Fixtures.*
import static org.junit.jupiter.api.Assertions.assertEquals

class AppointmentMapperTest extends Specification{

    def 'should map all fields'() {
        given:
        Appointment appointment = FromExisitngCommand.of(anAppointmentId(), Appointment.Status.CONFIRMED, aClientId(),
                aFacilityId(), aProductId(), anEmployeeId(), aBookingSlot(), aComment(), clock)
                .map(Appointment::fromExisting).get()
        AppointmentMapper mapper = Mappers.getMapper(AppointmentMapper.class)

        when:
        AppointmentEntity entity = mapper.map(appointment)
        Appointment mapped = mapper.map(entity, clock)

        then:
        mapped.id == appointment.id
        mapped.clientId == appointment.clientId
        mapped.facilityId == appointment.facilityId
        mapped.productId == appointment.productId
        mapped.slot == appointment.slot
        mapped.employeeId == appointment.employeeId
        mapped.comment == appointment.comment
        mapped.status == appointment.status
    }

    def 'should throw error during mapping'() {
        given:
        Appointment appointment = FromExisitngCommand.of(anAppointmentId(), Appointment.Status.CONFIRMED, aClientId(),
                aFacilityId(), aProductId(), anEmployeeId(), aBookingSlot(), aComment(), clock)
                .map(Appointment::fromExisting).get()
        AppointmentMapper mapper = Mappers.getMapper(AppointmentMapper.class)
        AppointmentEntity entity = mapper.map(appointment)
        entity.setStatus(null)

        when:
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class,
                () -> mapper.map(entity, clock))
        then:
        assertEquals("Could not map appointment entity", thrown.getMessage());
    }
}
