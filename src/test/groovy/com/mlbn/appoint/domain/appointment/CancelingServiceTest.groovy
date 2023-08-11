package com.mlbn.appoint.domain.appointment

import spock.lang.Specification

import static com.mlbn.appoint.shared.Fixtures.*

class CancelingServiceTest extends Specification {

    BookingService bookingService
    CancelingService cancelingService
    Appointments appointments
    Products products
    MockApplicationEventPublisher eventPublisher
    TestConfig config = new TestConfig()

    def setup() {
        bookingService = config.bookingService()
        products = config.products()
        cancelingService = config.cancelingService()
        appointments = config.appointments()
        eventPublisher = config.publisher() as MockApplicationEventPublisher
    }

    def 'should cancel appointment'() {
        given:
        products.save(aProduct())
        Appointment appointment = bookingService.book(aBookingCommand()).get()
        appointments.findById(appointment.id).get() == appointment
        appointments.findBookedSlots(appointment.slot)

        CancelCommand cancelCommand = CancelCommand.of(appointment.id).get()

        when:
        var result = cancelingService.cancel(cancelCommand)

        then:
        result.isRight()
        var bookingCanceled = result.get()
        var bookingCanceledId = bookingCanceled.id
        bookingCanceledId == appointment.id
        appointments.findById(bookingCanceledId).get().id == result.get().id
        appointments.findById(bookingCanceledId).get().status == Appointment.Status.CANCELED
        appointments.findBookedSlots(bookingCanceled.slot).isEmpty()
        eventPublisher.events().size() == 2
        eventPublisher.events().get(0).class == BookedEvent.class
        eventPublisher.events().get(1).class == CanceledEvent.class
    }

    def 'cannot cancel already canceled appointment'() {
        given:
        products.save(aProduct())
        Appointment appointment = bookingService.book(aBookingCommand()).get()
        appointments.findById(appointment.id).get() == appointment
        appointments.findBookedSlots(appointment.slot)

        CancelCommand cancelCommand = CancelCommand.of(appointment.id).get()
        cancelingService.cancel(cancelCommand)

        when:
        var result = cancelingService.cancel(cancelCommand)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages[0].contains("Wrong status, status = CANCELED")
    }
}
