package com.mlbn.appoint.integration.facility;

import com.mlbn.appoint.domain.appointment.*;
import com.mlbn.appoint.shared.api.ApiResponse;
import com.mlbn.appoint.shared.validation.Results;
import com.mlbn.appoint.domain.facility.FacilityId;
import com.mlbn.appoint.domain.facility.ProductCategory;
import com.mlbn.appoint.domain.facility.ProductId;
import com.mlbn.appoint.shared.vo.DateOfAppointment;
import com.mlbn.appoint.domain.facility.Facilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.util.UUID;

@Controller
@RequestMapping("/facility")
@RequiredArgsConstructor
class FacilityController {

    private final Products products;
    private final Facilities facilities;
    private final SlotService slots;

    @GetMapping
    ApiResponse findByProductCategory(@RequestParam String category) {
        return ApiResponse.from(Results.success(facilities.findByProductCategory(ProductCategory.valueOf(category))));
    }

    @GetMapping("/categories")
    ApiResponse getCategories() {
        return ApiResponse.from(Results.success(facilities.getCategories()));
    }

    @GetMapping("/{id}")
    ApiResponse findById(@PathVariable String id) {
        return ApiResponse.from(Results.success(facilities.findById(new FacilityId(UUID.fromString(id)))));
    }

    @GetMapping("/product/{id}")
    ApiResponse findProductDetails(@PathVariable String id) {
        return ApiResponse.from(Results.success(products.findById(new ProductId(UUID.fromString(id)))));
    }

    @GetMapping("/product/{id}/availableSlots")
    ApiResponse findAvailableSlots(@PathVariable String id, @RequestParam String date) {
        return ApiResponse.from(Results.success(slots.findAvailable(
                new BookingSlot(new ProductId(UUID.fromString(id)), DateOfAppointment.from(date).date().atStartOfDay(), Duration.ZERO))));
    }
}
