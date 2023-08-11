package com.mlbn.appoint.domain.appointment;

import com.mlbn.appoint.shared.vo.TimeSlot;
import com.mlbn.appoint.domain.facility.Product;
import com.mlbn.appoint.domain.facility.ProductId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Products {

    Product save(Product product);

    Optional<Product> findById(ProductId productId);

    Set<TimeSlot> findOpenSlots(BookingSlot bookingSlot);

    void deleteAll();

    List<Product> findAll();

}
