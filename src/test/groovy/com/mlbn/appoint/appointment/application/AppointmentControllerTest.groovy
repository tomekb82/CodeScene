package com.mlbn.appoint.appointment.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.mlbn.appoint.appointment.domain.*
import com.mlbn.appoint.common.Fixtures
import com.mlbn.appoint.facility.domain.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import static org.hamcrest.Matchers.containsString
import static org.hamcrest.Matchers.notNullValue
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTest extends Specification {

    @Autowired
    Appointments appointments

    @Autowired
    BookingService bookingService

    @Autowired
    Products products

    @Autowired
    Facilities facilities

    @Autowired
    MockMvc mockMvc

    def setup() {
        appointments.deleteAll()
        facilities.deleteAll()
    }

    def 'can book appointment'() {
        given:
        Product saved = products.save(Fixtures.aProduct())

        expect:
        mockMvc.perform(MockMvcRequestBuilders.post("/appointment/book")
                .content(asJsonString(BookingRequest.from(Fixtures.aBookingCommand(saved.id()))))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.result.id', notNullValue()))
                .andExpect(jsonPath('$.result.status', containsString("CONFIRMED")))
    }

    def 'can cancel appointment'() {
        given:
        Product saved = products.save(Fixtures.aProduct())

        when:
        var appointment = bookingService.book(Fixtures.aBookingCommand(saved.id())).get()

        then:
        mockMvc.perform(MockMvcRequestBuilders.post("/appointment/cancel")
                .content(asJsonString(new CancelingRequest(appointment.id.id())))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.result.id', notNullValue()))
                .andExpect(jsonPath('$.result.status', containsString("CANCELED")))
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
