package com.mlbn.appoint.facility.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface FacilityCategorySpringRepository extends JpaRepository<FacilityCategoryEntity, UUID> {
}
