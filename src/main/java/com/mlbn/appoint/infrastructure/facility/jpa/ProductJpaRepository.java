package com.mlbn.appoint.infrastructure.facility.jpa;

import com.mlbn.appoint.domain.appointment.BookingSlot;
import com.mlbn.appoint.shared.vo.DateOfAppointment;
import com.mlbn.appoint.domain.appointment.Products;
import com.mlbn.appoint.shared.vo.TimeSlot;
import com.mlbn.appoint.domain.facility.Product;
import com.mlbn.appoint.domain.facility.ProductId;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductJpaRepository implements Products {

    private final ProductSpringRepository repository;
    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @Override
    public Product save(Product product) {
        return mapper.map(repository.save(mapper.map(product)));
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return repository.findById(productId.id())
                .map(mapper::map);
    }

    @Override
    public Set<TimeSlot> findOpenSlots(BookingSlot bookingSlot) {
        DateOfAppointment dateOfAppointment = DateOfAppointment.from(bookingSlot.startDate());
        return repository.findOpenSlots(bookingSlot.productId().id(), dateOfAppointment.date(), dateOfAppointment.dayOfWeek())
                .stream()
                .map(e -> TimeSlot.of(e.getStartTime(), Duration.parse(e.getDuration())))
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll().stream()
                .map(mapper::map)
                .toList();
    }
}
