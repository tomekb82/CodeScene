package com.mlbn.appoint.domain.appointment


import com.mlbn.appoint.shared.vo.ClientId
import com.mlbn.appoint.domain.facility.EmployeeId
import com.mlbn.appoint.domain.facility.FacilityId
import com.mlbn.appoint.domain.facility.ProductId
import spock.lang.Specification

import static com.mlbn.appoint.shared.Fixtures.*

class BookingCommandTest extends Specification {

    def 'should validate client id (1)'() {
        when:
        var result = BookingCommand.of(
                null, aFacilityId(), anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Client ID is required!")
    }

    def 'should validate client id (2)'() {
        when:
        var result = BookingCommand.of(
                new ClientId(null), aFacilityId(), anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Client ID is required!")
    }

    def 'should validate facility (1)'() {
        when:
        var result = BookingCommand.of(
                aClientId(), null, anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Facility ID is required!")
    }

    def 'should validate facility (2)'() {
        when:
        var result = BookingCommand.of(
                aClientId(), new FacilityId(null), anEmployeeId(), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Facility ID is required!")
    }

    def 'should validate employee (1)'() {
        when:
        var result = BookingCommand.of(
                aClientId(), aFacilityId(), null, aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Service employee ID is required!")
    }

    def 'should validate employee (2)'() {
        when:
        var result = BookingCommand.of(
                aClientId(), aFacilityId(), new EmployeeId(null), aBookingSlot(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Service employee ID is required!")
    }

    def 'should validate product (1)'() {
        when:
        var result = BookingCommand.of(
                aClientId(), aFacilityId(), anEmployeeId(), aBookingSlot(null), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Product ID is required!")
    }

    def 'should validate product (2)'() {
        when:
        var result = BookingCommand.of(
                aClientId(), aFacilityId(), anEmployeeId(), aBookingSlot(new ProductId(null)), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages.contains("Product ID is required!")
    }

    def 'should validate booking slot for nullable slot values'() {
        given:
        BookingSlot slot = new BookingSlot(aProductId(),null, null)

        when:
        var result = BookingCommand.of(
                aClientId(), aFacilityId(), anEmployeeId(), slot, aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 2
        result.left().get().messages.contains("Start date is required!")
        result.left().get().messages.contains("Duration is required!")
    }

    def 'booking slot cannot should be in future and duration should be positive value'() {
        when:
        var result = BookingCommand.of(
                aClientId(), aFacilityId(), anEmployeeId(), aBookingSlotInThePastWithNegativeDuration(), aComment(), clock)

        then:
        result.isLeft()
        result.left().get().messages.size() == 2
        result.left().get().messages.contains("Duration could not be negative!")
        result.left().get().messages.contains("Start date is in the past!")
    }
}