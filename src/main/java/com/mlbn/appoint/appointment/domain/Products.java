package com.mlbn.appoint.appointment.domain;

import com.mlbn.appoint.common.vo.TimeSlot;
import com.mlbn.appoint.facility.domain.Product;
import com.mlbn.appoint.facility.domain.ProductId;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Products {

    Product save(Product product);

    Optional<Product> findById(ProductId productId);

    Set<TimeSlot> findOpenSlotsForDayOfWeek(ProductId productId, DayOfWeek dayOfWeek);

    void deleteAll();

    List<Product> findAll();

}
