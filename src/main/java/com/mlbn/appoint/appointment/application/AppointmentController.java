package com.mlbn.appoint.appointment.application;

import com.mlbn.appoint.appointment.domain.*;
import com.mlbn.appoint.common.api.ApiResponse;
import com.mlbn.appoint.common.validation.Results;
import com.mlbn.appoint.facility.domain.FacilityId;
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
