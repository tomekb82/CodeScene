package com.mlbn.appoint.appointment.domain

import spock.lang.Specification

import static com.mlbn.appoint.common.Fixtures.*

class BookingServiceTest extends Specification {

    BookingService bookingService
    MockApplicationEventPublisher eventPublisher
    Appointments appointments
    Products products
    TestConfig config = new TestConfig()

    def setup() {
        bookingService = config.bookingService()
        appointments = config.appointments()
        products = config.products()
        eventPublisher = config.publisher() as MockApplicationEventPublisher
    }

    def 'should book appointment'() {
        when:
        products.save(aProduct())
        var result = bookingService.book(aBookingCommand())

        then:
        result.isRight()
        var appointment = result.get()
        var appointmentId = appointment.id
        appointments.findById(appointmentId).get() == result.get()
        appointments.findById(appointmentId).get().id == result.get().id
        appointments.findById(appointmentId).get().status == Appointment.Status.CONFIRMED
        appointments.findBookedSlotsForDate(appointment.getProductId(), new DateOfAppointment(appointment.getSlot().startDate().toLocalDate())) == Set.of(appointment.getSlot())
        eventPublisher.events().size() == 1
        eventPublisher.events().get(0).class == BookedEvent.class
    }

    def 'cannot book the same appointment twice'() {
        given:
        products.save(aProduct())
        bookingService.book(aBookingCommand())

        when:
        var result = bookingService.book(aBookingCommand())

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages[0].contains("Product slot is not available")
    }
}
