package com.mlbn.appoint.domain.appointment


import com.mlbn.appoint.shared.vo.ClientId
import com.mlbn.appoint.domain.facility.EmployeeId
import com.mlbn.appoint.domain.facility.FacilityId
import com.mlbn.appoint.domain.facility.ProductId
import com.mlbn.appoint.shared.vo.Comment
import spock.lang.Specification

import static com.mlbn.appoint.shared.Fixtures.*

class AppointmentTest extends Specification {

    def 'should book an appointment for active product and available dates'() {
        given:
        ClientId clientId = aClientId()
        FacilityId facilityId= aFacilityId()
        ProductId productId= aProductId()
        EmployeeId employeeId = anEmployeeId();
        BookingSlot slot = aBookingSlot()
        Comment comment = aComment()
        BookingCommand command = BookingCommand.of(clientId, facilityId, employeeId, slot, comment, clock).get()

        when:
        var result = Appointment.book(command)

        then:
        result.isRight()
        result.get().getId() != null
        result.get().getClientId() == clientId
        result.get().getFacilityId() == facilityId
        result.get().getSlot().productId() == productId
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
