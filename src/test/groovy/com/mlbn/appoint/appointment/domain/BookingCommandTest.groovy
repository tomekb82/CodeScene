package com.mlbn.appoint.appointment.domain

import com.mlbn.appoint.facility.domain.EmployeeId
import com.mlbn.appoint.facility.domain.FacilityId
import com.mlbn.appoint.facility.domain.ProductId
import spock.lang.Specification

import static com.mlbn.appoint.common.Fixtures.*

class BookingCommandTest extends Specification {

    def 'should validate client id (1)'() {
        when:
        var result = BookingCommand.of(
                null, aFacilityId(), aProductId(), anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Client ID is required!")
    }

    def 'should validate client id (2)'() {
        when:
        var result = BookingCommand.of(
                new ClientId(null), aFacilityId(), aProductId(), anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Client ID is required!")
    }

    def 'should validate facility (1)'() {
        when:
        var result = BookingCommand.of(
                aClientId(), null, aProductId(), anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Facility ID is required!")
    }

    def 'should validate facility (2)'() {
        when:
        var result = BookingCommand.of(
                aClientId(), new FacilityId(null), aProductId(), anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Facility ID is required!")
    }

    def 'should validate employee (1)'() {
        when:
        var result = BookingCommand.of(
                aClientId(), aFacilityId(), aProductId(), null, aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Service employee ID is required!")
    }

    def 'should validate employee (2)'() {
        when:
        var result = BookingCommand.of(
                aClientId(), aFacilityId(), aProductId(), new EmployeeId(null), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Service employee ID is required!")
    }

    def 'should validate product (1)'() {
        when:
        var result = BookingCommand.of(
                aClientId(), aFacilityId(), null, anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Product ID is required!")
    }

    def 'should validate product (2)'() {
        when:
        var result = BookingCommand.of(
                aClientId(), aFacilityId(), new ProductId(null), anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Product ID is required!")
    }

    def 'should validate booking slot for nullable slot values'() {
        given:
        BookingSlot slot = new BookingSlot(null, null)

        when:
        var result = BookingCommand.of(
                aClientId(), aFacilityId(), aProductId(), anEmployeeId(), slot, aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 2
        result.left().get().messages.contains("Start date is required!")
        result.left().get().messages.contains("Duration is required!")
    }

    def 'booking slot cannot should be in future and duration should be positive value'() {
        when:
        var result = BookingCommand.of(
                aClientId(), aFacilityId(), aProductId(), anEmployeeId(), aBookingSlotInThePastWithNegativeDuration(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 2
        result.left().get().messages.contains("Duration could not be negative!")
        result.left().get().messages.contains("Start date is in the past!")
    }
}