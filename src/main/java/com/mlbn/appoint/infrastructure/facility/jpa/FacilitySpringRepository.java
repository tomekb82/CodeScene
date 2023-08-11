package com.mlbn.appoint.infrastructure.facility.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;
import java.util.UUID;

interface FacilitySpringRepository extends JpaRepository<FacilityEntity, UUID> {

    @Query("from FacilityEntity f join f.products p where p.category = :category")
    Set<FacilityEntity> findByProductsCategory(String category);
}
