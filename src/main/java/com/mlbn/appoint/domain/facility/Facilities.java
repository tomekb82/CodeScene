package com.mlbn.appoint.domain.facility;

import java.util.Optional;
import java.util.Set;

public interface Facilities {

    Facility save(Facility facility);

    Set<Facility> findByProductCategory(ProductCategory productCategory);

    void deleteAll();

    Optional<Facility> findById(FacilityId facilityId);

    Set<String> getCategories();

    void saveCategory(String name);
}
