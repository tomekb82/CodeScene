package com.mlbn.appoint.integration.appointment

import com.mlbn.appoint.domain.appointment.AppointEventListener
import com.mlbn.appoint.domain.appointment.Appointment
import com.mlbn.appoint.domain.appointment.Appointments
import com.mlbn.appoint.domain.appointment.BookedEvent
import com.mlbn.appoint.domain.appointment.BookingCommand
import com.mlbn.appoint.domain.appointment.BookingService
import com.mlbn.appoint.domain.appointment.BookingSlot
import com.mlbn.appoint.domain.appointment.CancelCommand
import com.mlbn.appoint.domain.appointment.CanceledEvent
import com.mlbn.appoint.domain.appointment.CancelingService
import com.mlbn.appoint.domain.appointment.Products
import com.mlbn.appoint.domain.facility.Facilities
import com.mlbn.appoint.domain.facility.Product
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue
import static com.mlbn.appoint.shared.Fixtures.*

@SpringBootTest
class CancelingServiceIntegrationTest extends Specification {

    @Autowired
    BookingService bookingService

    @Autowired
    CancelingService cancelingService

    @Autowired
    Appointments appointments

    @Autowired
    Products products

    @Autowired
    Facilities facilities;

    @Autowired
    AppointEventListener eventListener

    @BeforeEach
    def 'cleanup'() {
        facilities.deleteAll()
        appointments.deleteAll()
        eventListener.clear()
    }

    def 'can cancel an appointment'() {
        given:
        Product product = aProduct()
        Product savedProduct = products.save(product)
        BookingCommand command = BookingCommand.of(aClientId(),
                aFacilityId(),
                anEmployeeId(),
                aBookingSlot(savedProduct.id()),
                aComment(),
                clock).get()
        Appointment appointment = bookingService.book(command).get()
        CancelCommand cancelCommand = CancelCommand.of(appointment.getId()).get()

        when:
        var result = cancelingService.cancel(cancelCommand)

        then:
        assertTrue(result.isRight())
        var canceling = result.get()
        var cancelingId = canceling.getId()
        Appointment saved = appointments.findById(cancelingId).get()
        assertEquals(saved.getId(), cancelingId)
        assertEquals(saved.getStatus(), Appointment.Status.CANCELED)

        Set<BookingSlot> bookedSlots = appointments.findBookedSlots(appointment.getSlot())
        assertEquals(bookedSlots.size(), 0)

        var events = eventListener.events.stream()
                .filter(event -> event.getAggregateId().id() == appointment.id.id())
                .toList()
        assertEquals(2, events.size())
        assertTrue(events.get(0) instanceof BookedEvent)
        assertTrue(events.get(1) instanceof CanceledEvent)
    }
}
