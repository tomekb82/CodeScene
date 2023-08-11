package com.mlbn.appoint.integration.appointment

import com.mlbn.appoint.domain.appointment.AppointEventListener
import com.mlbn.appoint.domain.appointment.Appointment
import com.mlbn.appoint.domain.appointment.Appointments
import com.mlbn.appoint.domain.appointment.BookedEvent
import com.mlbn.appoint.domain.appointment.BookingCommand
import com.mlbn.appoint.domain.appointment.BookingService
import com.mlbn.appoint.domain.appointment.BookingSlot
import com.mlbn.appoint.domain.appointment.Products
import com.mlbn.appoint.domain.facility.Facilities
import com.mlbn.appoint.shared.vo.Money
import com.mlbn.appoint.domain.facility.Employee
import com.mlbn.appoint.domain.facility.Product
import com.mlbn.appoint.domain.facility.ProductCategory
import com.mlbn.appoint.domain.facility.ProductStatus
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue
import static com.mlbn.appoint.shared.Fixtures.*

@SpringBootTest
class BookingServiceIntegrationTest extends Specification {

    @Autowired
    BookingService bookingService

    @Autowired
    Appointments appointments

    @Autowired
    Products products

    @Autowired
    Facilities facilities

    @Autowired
    AppointEventListener eventListener

    @BeforeEach
    def 'setup'() {
        facilities.deleteAll()
        appointments.deleteAll()
        eventListener.clear()
    }

    def 'can book an appointment'() {
        given:
        Product product = new Product(aProductId(),
                ProductStatus.ACTIVE,
                "name",
                "description",
                ProductCategory.BEARD_SHAPING,
                Money.of(new BigDecimal("200")).get(),
                Set.of(new Employee(anEmployeeId(), "T", "B")),
                Set.of(aConfigurationSlot()),
                Set.of());
        Product savedProduct = products.save(product)
        BookingCommand command = BookingCommand.of(aClientId(),
                aFacilityId(),
                anEmployeeId(),
                aBookingSlot(savedProduct.id()),
                aComment(),
                clock).get()

        when:
        var result = bookingService.book(command)

        then:
        assertTrue(result.isRight())
        var appointment = result.get()
        var appointmentId = appointment.getId()
        Appointment saved = appointments.findById(appointmentId).get()
        assertEquals(saved.getId(), appointmentId)
        assertEquals(saved.getStatus(), Appointment.Status.CONFIRMED)
        assertEquals(appointment.getSlot().productId(), command.getBookingSlot().productId())

        Set<BookingSlot> bookedSlots = appointments.findBookedSlots(appointment.getSlot())
        assertEquals(bookedSlots.size(), 1)
        BookingSlot slot = bookedSlots.stream().toList().get(0)
        assertTrue(slot.isEqualTo(command.getBookingSlot()))
        assertEquals(slot.duration(), command.getBookingSlot().duration())

        assertEquals(1, eventListener.events.size())
        assertTrue(eventListener.events.get(0) instanceof BookedEvent)
    }

    def 'cannot book appointment for closed slot'() {
        given:
        Product product = new Product(aProductId(),
                ProductStatus.ACTIVE,
                "name",
                "description",
                ProductCategory.BEARD_SHAPING,
                Money.of(new BigDecimal("200")).get(),
                Set.of(new Employee(anEmployeeId(), "T", "B")),
                Set.of(aConfigurationSlot()),
                Set.of(aClosedSlot()));
        Product savedProduct = products.save(product)
        BookingCommand command = BookingCommand.of(aClientId(),
                aFacilityId(),
                anEmployeeId(),
                aBookingSlot(savedProduct.id()),
                aComment(),
                clock).get()

        when:
        var result = bookingService.book(command)

        then:
        assertTrue(result.isLeft())
        result.left().get().messages.size() == 1
        result.left().get().messages[0].contains("Product slot is not available")
    }

    def 'can book 2 appointments for the same product but in different slots'() {
        when:
        Product savedProduct = products.save(aProduct())
        bookingService.book(aBookingCommand(savedProduct.id())).get()
        var result = bookingService.book(aBookingCommand(aBookingSlot2(savedProduct.id())))

        then:
        assertTrue(result.isRight())
        var appointment = result.get()
        var appointmentId = appointment.getId()
        Appointment saved = appointments.findById(appointmentId).get()
        assertEquals(saved.getId(), appointmentId)
        assertEquals(saved.getStatus(), Appointment.Status.CONFIRMED)
        assertEquals(appointment.getSlot().productId(), savedProduct.id())

        Set<BookingSlot> bookedSlots = appointments.findBookedSlots(appointment.getSlot())
        assertEquals(bookedSlots.size(), 2)

        assertEquals(2, eventListener.events.size())
        assertTrue(eventListener.events.get(0) instanceof BookedEvent)
        assertTrue(eventListener.events.get(1) instanceof BookedEvent)
    }
}
