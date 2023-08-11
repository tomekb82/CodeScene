package com.mlbn.appoint.domain.appointment;

import com.mlbn.appoint.shared.vo.DateOfAppointment;
import com.mlbn.appoint.shared.vo.TimeSlot;
import com.mlbn.appoint.domain.facility.ConfigurationSlot;
import com.mlbn.appoint.domain.facility.Product;
import com.mlbn.appoint.domain.facility.ProductId;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryProducts implements Products {

    Map<UUID, Product> db = new ConcurrentHashMap<>();

    @Override
    public Product save(Product product) {
        db.put(product.id().id(), product);
        return product;
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return Optional.empty();
    }

    @Override
    public Set<TimeSlot> findOpenSlots(BookingSlot bookingSlot) {
        return Optional.ofNullable(db.get(bookingSlot.productId().id()))
                .filter(product -> product.id().equals(bookingSlot.productId()))
                .filter(Product::isActive)
                .map(Product::configurationSlots)
                .flatMap(slots -> slots.stream()
                        .filter(slot -> slot.isSameDay(DateOfAppointment.from(bookingSlot.startDate()).dayOfWeek()))
                        .findFirst())
                .map(ConfigurationSlot::slots)
                .orElse(Set.of());
    }

    @Override
    public void deleteAll() {
    }

    @Override
    public List<Product> findAll() {
        return null;
    }
}
