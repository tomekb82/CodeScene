package com.mlbn.appoint.infrastructure.facility.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface FacilityCategorySpringRepository extends JpaRepository<FacilityCategoryEntity, UUID> {
}
