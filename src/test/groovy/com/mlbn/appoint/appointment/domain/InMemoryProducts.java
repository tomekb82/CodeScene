package com.mlbn.appoint.appointment.domain;

import com.mlbn.appoint.common.vo.TimeSlot;
import com.mlbn.appoint.facility.domain.ConfigurationSlot;
import com.mlbn.appoint.facility.domain.Product;
import com.mlbn.appoint.facility.domain.ProductId;

import java.time.DayOfWeek;
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
    public Set<TimeSlot> findOpenSlotsForDayOfWeek(ProductId productId, DayOfWeek dayOfWeek) {
        return Optional.ofNullable(db.get(productId.id()))
                .filter(product -> product.id().equals(productId))
                .filter(Product::isActive)
                .map(Product::configurationSlots)
                .flatMap(slots -> slots.stream()
                        .filter(slot -> slot.isSameDay(dayOfWeek))
                        .findFirst())
                .map(ConfigurationSlot::openSlots)
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
