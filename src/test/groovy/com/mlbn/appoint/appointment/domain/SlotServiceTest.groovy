package com.mlbn.appoint.appointment.domain

import com.mlbn.appoint.common.vo.TimeSlot
import spock.lang.Specification

import java.time.DayOfWeek
import java.time.Duration

import static com.mlbn.appoint.common.Fixtures.*

class SlotServiceTest extends Specification {

    Appointments appointments = Mock(Appointments)
    Products products = Mock(Products)
    TestConfig config = new TestConfig()

    def 'should verify booking slots'() {
        given:
        products.findOpenSlotsForDayOfWeek(aProductId(), DayOfWeek.THURSDAY) >> openSlots
        appointments.findBookedSlotsForDate(aProductId(), DateOfAppointment.from(aBookingSlot())) >> bookedSlots
        SlotService slotService = new SlotService(appointments, products, config.fixedClock())

        expect:
        slotService.isAvailable(aBookingCommand()).isRight() == result

        where:
        openSlots   | bookedSlots            | result
        openSlots() | bookedSlots()          | false
        openSlots() | null                   | true
        openSlots() | Set.<BookingSlot> of() | true
    }

    def 'should not accept empty open slots'() {
        given:
        products.findOpenSlotsForDayOfWeek(aProductId(), DayOfWeek.THURSDAY) >> openSlots
        appointments.findBookedSlotsForDate(aProductId(), DateOfAppointment.from(aBookingSlot())) >> null
        SlotService slotService = new SlotService(appointments, products, config.fixedClock())

        expect:
        slotService.isAvailable(aBookingCommand()).isLeft()
        slotService.isAvailable(aBookingCommand()).left().get().messages[0] == "List of open openSlots should not be empty"

        where:
        openSlots << [null, Set.<TimeSlot> of()]
    }

    def 'should return open slots'() {
        given:
        products.findOpenSlotsForDayOfWeek(aProductId(), DayOfWeek.THURSDAY) >> openSlots
        appointments.findBookedSlotsForDate(aProductId(), DateOfAppointment.from(aBookingSlot())) >> bookedSlots
        SlotService slotService = new SlotService(appointments, products, config.fixedClock())

        expect:
        slotService.findAvailable(aProductId(), aBookingSlot().startDate().format(SlotService.FORMATTER)) == result

        where:
        openSlots   | bookedSlots            | result
        openSlots() | bookedSlots()          | Set.<TimeSlot> of()
        openSlots() | null                   | openSlots()
        openSlots() | Set.<BookingSlot> of() | openSlots()
    }

    private static Set<BookingSlot> bookedSlots() {
        return Set.of(new BookingSlot(aStartDate(), Duration.ofHours(1)))
    }

    private static Set<TimeSlot> openSlots() {
        return Set.of(new TimeSlot(aStartDate().toLocalTime(), Duration.ofHours(1)))
    }
}
