package com.mlbn.appoint.infrastructure.facility.jpa;

import com.mlbn.appoint.domain.facility.Facilities;
import com.mlbn.appoint.domain.facility.Facility;
import com.mlbn.appoint.domain.facility.FacilityId;
import com.mlbn.appoint.domain.facility.ProductCategory;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FacilityJpaRepository implements Facilities {

    private final FacilitySpringRepository repository;
    private final FacilityCategorySpringRepository categoryRepository;
    private final FacilityMapper mapper = Mappers.getMapper(FacilityMapper.class);

    @Override
    public Facility save(Facility facility) {
        return mapper.map(repository.save(mapper.map(facility)));
    }

    @Override
    public Set<Facility> findByProductCategory(ProductCategory productCategory) {
        return repository.findByProductsCategory(productCategory.name()).stream()
                .map(mapper::map)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public Optional<Facility> findById(FacilityId facilityId) {
        return repository.findById(facilityId.id())
                .map(mapper::map);
    }

    @Override
    public Set<String> getCategories() {
        return categoryRepository.findAll().stream()
                .map(FacilityCategoryEntity::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public void saveCategory(String name) {
        categoryRepository.save(new FacilityCategoryEntity(name));
    }
}
