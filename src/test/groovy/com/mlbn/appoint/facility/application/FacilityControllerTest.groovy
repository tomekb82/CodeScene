package com.mlbn.appoint.facility.application

import com.mlbn.appoint.appointment.domain.Facilities
import com.mlbn.appoint.facility.domain.Facility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import static org.hamcrest.Matchers.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import static com.mlbn.appoint.common.Fixtures.*

@SpringBootTest
@AutoConfigureMockMvc
class FacilityControllerTest extends Specification {

    @Autowired
    Facilities facilities

    @Autowired
    MockMvc mockMvc

    def setup() {
        facilities.deleteAll()
    }

    def 'can read all fields from facility'() {
        given:
        Facility saved = facilities.save(aFacility())

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/facility/" + saved.id().id())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.result.id', notNullValue()))
                .andExpect(jsonPath('$.result.name', containsString(saved.name())))
                .andExpect(jsonPath('$.result.address', notNullValue()))
                .andExpect(jsonPath('$.result.phoneNumber', notNullValue()))
                .andExpect(jsonPath('$.result.products', notNullValue()))
    }

    def 'can get categories'() {
        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/facility/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.result', containsInAnyOrder(
                        'MALE_HAIRCUT',
                        'FAMALE_HAIRCUT',
                        'SKIN_FADE',
                        'HAIRCUT_AND_BEARD',
                        'BEARD_TRIM',
                        'KIDS_HAIRCUT',
                        'GEL_MANICURE',
                        'EYELASH_MANICURE',
                        'HAIR_DESIGN',
                        'BEARD_SHAPING')))
    }
}
