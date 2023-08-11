package com.mlbn.appoint.domain.appointment


import com.mlbn.appoint.domain.facility.EmployeeId
import com.mlbn.appoint.domain.facility.FacilityId
import com.mlbn.appoint.domain.facility.ProductId
import com.mlbn.appoint.shared.vo.ClientId
import spock.lang.Specification

import static com.mlbn.appoint.shared.Fixtures.*

class FromExisitngCommandTest extends Specification {

    def 'should validate appointment id (1)'() {
        when:
        var result = FromExisitngCommand.of(null, Appointment.Status.CONFIRMED,
                aClientId(), aFacilityId(), anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Appointment ID is required!")
    }

    def 'should validate appointment id (2)'() {
        when:
        var result = FromExisitngCommand.of(new AppointmentId(null), Appointment.Status.CONFIRMED,
                aClientId(), aFacilityId(), anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Appointment ID is required!")
    }

    def 'should validate status'() {
        when:
        var result = FromExisitngCommand.of(anAppointmentId(), null,
                aClientId(), aFacilityId(), anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Appointment status is required!")
    }

    def 'should validate client id (1)'() {
        when:
        var result = FromExisitngCommand.of(anAppointmentId(), Appointment.Status.CONFIRMED,
                null, aFacilityId(), anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Client ID is required!")
    }

    def 'should validate client id (2)'() {
        when:
        var result = FromExisitngCommand.of(anAppointmentId(), Appointment.Status.CONFIRMED,
                new ClientId(null), aFacilityId(), anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Client ID is required!")
    }

    def 'should validate facility (1)'() {
        when:
        var result = FromExisitngCommand.of(anAppointmentId(), Appointment.Status.CONFIRMED,
                aClientId(), null, anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Facility ID is required!")
    }

    def 'should validate facility (2)'() {
        when:
        var result = FromExisitngCommand.of(anAppointmentId(), Appointment.Status.CONFIRMED,
                aClientId(), new FacilityId(null), anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Facility ID is required!")
    }

    def 'should validate employee (1)'() {
        when:
        var result = FromExisitngCommand.of(anAppointmentId(), Appointment.Status.CONFIRMED,
                aClientId(), aFacilityId(), null, aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Service employee ID is required!")
    }

    def 'should validate employee (2)'() {
        when:
        var result = FromExisitngCommand.of(anAppointmentId(), Appointment.Status.CONFIRMED,
                aClientId(), aFacilityId(), new EmployeeId(null), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Service employee ID is required!")
    }

    def 'should validate product (1)'() {
        when:
        var result = FromExisitngCommand.of(anAppointmentId(), Appointment.Status.CONFIRMED,
                aClientId(), aFacilityId(), anEmployeeId(), aBookingSlot(null), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Product ID is required!")
    }

    def 'should validate product (2)'() {
        when:
        var result = FromExisitngCommand.of(anAppointmentId(), Appointment.Status.CONFIRMED,
                aClientId(), aFacilityId(), anEmployeeId(), aBookingSlot(new ProductId(null)), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Product ID is required!")
    }

    def 'should validate booking slot for nullable slot values'() {
        given:
        BookingSlot slot = new BookingSlot(aProductId(), null, null)

        when:
        var result = FromExisitngCommand.of(anAppointmentId(), Appointment.Status.CONFIRMED,
                aClientId(), aFacilityId(), anEmployeeId(), slot, aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 2
        result.left().get().messages.contains("Start date is required!")
        result.left().get().messages.contains("Duration is required!")
    }

    def 'should validate booking slot'() {
        when:
        var result = FromExisitngCommand.of(anAppointmentId(), Appointment.Status.CONFIRMED,
                aClientId(), aFacilityId(), anEmployeeId(), aBookingSlotInThePastWithNegativeDuration(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 2
        result.left().get().messages.contains("Duration could not be negative!")
        result.left().get().messages.contains("Start date is in the past!")
    }
}
