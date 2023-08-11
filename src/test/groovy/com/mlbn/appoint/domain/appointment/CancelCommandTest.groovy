package com.mlbn.appoint.domain.appointment

import com.mlbn.appoint.domain.appointment.AppointmentId
import com.mlbn.appoint.domain.appointment.CancelCommand
import spock.lang.Specification

class CancelCommandTest extends Specification {

    def 'should validate appointment id'() {
        given:
        AppointmentId id = null

        when:
        var result = CancelCommand.of(id)

        then:
        result.isLeft()
        result.left().get().messages.size() == 1
        result.left().get().messages[0] == "Appointment ID is required!"
    }
}
