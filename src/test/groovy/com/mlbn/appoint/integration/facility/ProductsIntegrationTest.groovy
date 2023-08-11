package com.mlbn.appoint.integration.facility

import com.mlbn.appoint.domain.appointment.Products
import com.mlbn.appoint.shared.vo.TimeSlot
import com.mlbn.appoint.domain.facility.ClosedSlot
import com.mlbn.appoint.domain.facility.ProductId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static com.mlbn.appoint.shared.Fixtures.*

@SpringBootTest
class ProductsIntegrationTest extends Specification {

    @Autowired
    Products products

    def 'should return open slots when partially closed during a day'() {
        given:
        ClosedSlot closedSlot = new ClosedSlot(aStartDate().toLocalDate(), closedSlots as Set<TimeSlot>)
        ProductId productId = products.save(aProduct(Set.of(closedSlot))).id()

        expect:
        products.findOpenSlots(aBookingSlot(productId)).size() == result

        where:
        closedSlots               | result
        Set.of()                  | 2 // no closed slots
        Set.of(aSlot2())          | 1 // 1 closed slot
        Set.of(aSlot(), aSlot2()) | 0 // 2 closed slots
        Set.of(aSlot(-3, 8 * 60)) | 0 // closed for all day
        Set.of(aSlot(2, 120))     | 1 // partially closed  |closedStart|slotStart|closedEnd|slotEnd|
        Set.of(aSlot(2, 300))     | 1 // partially closed  |closedStart|slotStart|slotEnd|closedEnd|
        Set.of(aSlot(4, 300))     | 1 // partially closed  |slotStart|closedStart|slotEnd|closedEnd|
        Set.of(aSlot(4, 30))      | 1 // partially closed  |slotStart|closedStart|closedEnd|slotEnd|
        Set.of(aSlot(-3, 60))     | 2 // no closed slots when ranges not overlapping |closedStart|closedEnd|slotStart|slotEnd|
    }
}
