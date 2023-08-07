package com.mlbn.appoint.appointment.domain

import com.mlbn.appoint.facility.domain.Product
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static com.mlbn.appoint.common.Fixtures.*
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

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
                savedProduct.id(),
                anEmployeeId(),
                aBookingSlot(),
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

        Set<BookingSlot> bookedSlots = appointments.findBookedSlotsForDate(appointment.getProductId(), DateOfAppointment.from(appointment.getSlot()))
        assertEquals(bookedSlots.size(), 0)

        assertEquals(2, eventListener.events.size())
        assertTrue(eventListener.events.get(0) instanceof BookedEvent)
        assertTrue(eventListener.events.get(1) instanceof CanceledEvent)
    }
}
