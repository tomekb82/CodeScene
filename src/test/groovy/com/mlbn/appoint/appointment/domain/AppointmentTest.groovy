package com.mlbn.appoint.appointment.domain

import com.mlbn.appoint.facility.domain.EmployeeId
import com.mlbn.appoint.facility.domain.FacilityId
import com.mlbn.appoint.facility.domain.ProductId
import spock.lang.Specification

import static com.mlbn.appoint.common.Fixtures.*

class AppointmentTest extends Specification {

    def 'should book an appointment for active product and available dates'() {
        given:
        ClientId clientId = aClientId()
        FacilityId facilityId= aFacilityId()
        ProductId productId= aProductId()
        EmployeeId employeeId = anEmployeeId();
        BookingSlot slot = aBookingSlot()
        Comment comment = aComment()
        BookingCommand command = BookingCommand.of(clientId, facilityId, productId, employeeId, slot, comment, clock).get()

        when:
        var result = Appointment.book(command)

        then:
        result.isRight()
        result.get().getId() != null
        result.get().getClientId() == clientId
        result.get().getFacilityId() == facilityId
        result.get().getProductId() == productId
        result.get().getEmployeeId() == employeeId
        result.get().getSlot() == slot
        result.get().getComment() == comment
        result.get().status == Appointment.Status.CONFIRMED
    }

    def 'should cancel already confirmed appointment'() {
        given:
        BookingCommand command = aBookingCommand()
        Appointment appointment = Appointment.book(command).get()

        when:
        var result = appointment.cancel()

        then:
        result.isRight()
        result.get().status == Appointment.Status.CANCELED
    }

}
