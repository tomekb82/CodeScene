package com.mlbn.appoint.facility.application;

import com.mlbn.appoint.appointment.domain.Facilities;
import com.mlbn.appoint.appointment.domain.Slots;
import com.mlbn.appoint.appointment.domain.Products;
import com.mlbn.appoint.common.api.ApiResponse;
import com.mlbn.appoint.common.validation.Results;
import com.mlbn.appoint.facility.domain.FacilityId;
import com.mlbn.appoint.facility.domain.ProductCategory;
import com.mlbn.appoint.facility.domain.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/facility")
@RequiredArgsConstructor
class FacilityController {

    private final Products products;
    private final Facilities facilities;
    private final Slots slots;

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

    @GetMapping("/product/{id}/openSlots")
    ApiResponse findAvailableSlots(@PathVariable String id, @RequestParam String date) {
        return ApiResponse.from(Results.success(slots.findAvailable(new ProductId(UUID.fromString(id)), date)));
    }
}
