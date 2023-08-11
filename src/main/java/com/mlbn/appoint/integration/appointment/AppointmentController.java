package com.mlbn.appoint.integration.appointment;

import com.mlbn.appoint.domain.appointment.AppointmentId;
import com.mlbn.appoint.shared.api.ApiResponse;
import com.mlbn.appoint.shared.validation.Results;
import com.mlbn.appoint.domain.appointment.Appointments;
import com.mlbn.appoint.domain.appointment.BookingService;
import com.mlbn.appoint.domain.appointment.CancelingService;
import com.mlbn.appoint.domain.facility.FacilityId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.util.UUID;

@Controller
@RequestMapping("/appointment")
@RequiredArgsConstructor
class AppointmentController {

    private final BookingService bookingService;
    private final CancelingService cancelingService;
    private final Appointments appointments;
    private final Clock clock;

    @PostMapping("/book")
    ApiResponse book(@RequestBody BookingRequest request) {
        return ApiResponse.from(request.toCommand(clock)
                .flatMap(bookingService::book));
    }

    @PostMapping("/cancel")
    ApiResponse cancel(@RequestBody CancelingRequest request) {
        return ApiResponse.from(request.toCommand()
                .flatMap(cancelingService::cancel));
    }

    @GetMapping
    ApiResponse findAll() {
        return ApiResponse.from(Results.success(appointments.findAll()));
    }

    @GetMapping("/{id}")
    ApiResponse findById(@PathVariable UUID id) {
        return ApiResponse.from(Results.success(appointments.findById(new AppointmentId(id))));
    }

    @GetMapping("/facility/{id}")
    ApiResponse findByFacilityId(@PathVariable UUID id) {
        return ApiResponse.from(Results.success(appointments.findByFacilityId(new FacilityId(id))));
    }
}
