package com.mlbn.appoint.domain.appointment

import com.mlbn.appoint.shared.vo.TimeSlot
import spock.lang.Specification

import static com.mlbn.appoint.shared.Fixtures.*

class SlotServiceTest extends Specification {

    Appointments appointments = Mock(Appointments)
    Products products = Mock(Products)
    TestConfig config = new TestConfig()

    def 'should verify booking slots'() {
        given:
        products.findOpenSlots(aBookingSlot2()) >> openSlots
        appointments.findBookedSlots(aBookingSlot2()) >> bookedSlots
        SlotService slotService = new SlotService(appointments, products, config.fixedClock())

        expect:
        slotService.isAvailable(aBookingSlot2()).isRight() == result

        where:
        openSlots            | bookedSlots             | result
        Set.of(aSlot(3, 60)) | Set.of(aBookingSlot2()) | false
        Set.of(aSlot(3, 60)) | Set.<BookingSlot> of()  | true
    }

    def 'should not accept empty open slots'() {
        given:
        products.findOpenSlots(aBookingSlot2()) >> Set.<TimeSlot> of()
        appointments.findBookedSlots(aBookingSlot2()) >> Set.<BookingSlot> of()
        SlotService slotService = new SlotService(appointments, products, config.fixedClock())

        expect:
        slotService.isAvailable(aBookingSlot2()).isLeft()
        slotService.isAvailable(aBookingSlot2()).left().get().messages[0] == "List of open slots should not be empty"
    }

    def 'should return open slots'() {
        given:
        products.findOpenSlots(aBookingSlot2()) >> openSlots
        appointments.findBookedSlots(aBookingSlot2()) >> bookedSlots
        SlotService slotService = new SlotService(appointments, products, config.fixedClock())

        expect:
        slotService.findAvailable(aBookingSlot2()) == result

        where:
        openSlots            | bookedSlots             | result
        Set.of(aSlot(3, 60)) | Set.of(aBookingSlot2()) | Set.<TimeSlot> of()
        Set.of(aSlot(3, 60)) | Set.<BookingSlot> of()  | Set.of(aSlot(3, 60))
    }
}