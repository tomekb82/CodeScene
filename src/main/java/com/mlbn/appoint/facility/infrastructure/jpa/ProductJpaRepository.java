package com.mlbn.appoint.facility.infrastructure.jpa;

import com.mlbn.appoint.appointment.domain.Products;
import com.mlbn.appoint.common.vo.TimeSlot;
import com.mlbn.appoint.facility.domain.Product;
import com.mlbn.appoint.facility.domain.ProductId;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
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
    public Set<TimeSlot> findOpenSlotsForDayOfWeek(ProductId productId, DayOfWeek dayOfWeek) {
        return repository.findSlotsForDayOfWeek(productId.id(), dayOfWeek)
                .stream()
                .map(e -> TimeSlot.of(e.getStartDate(), Duration.parse(e.getDuration())))
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
