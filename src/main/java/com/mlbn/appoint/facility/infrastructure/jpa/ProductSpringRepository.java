package com.mlbn.appoint.facility.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface ProductSpringRepository extends JpaRepository<ProductEntity, UUID> {

    @Query("SELECT DISTINCT os FROM ProductEntity p " +
            "JOIN p.slots c " +
            "JOIN c.openSlots os " +
            "WHERE p.id = :productId " +
            "AND p.status = 'ACTIVE' " +
            "AND c.dayOfWeek = :dayOfWeek " +
            "AND NOT EXISTS (" +
            "SELECT 1 FROM ProductEntity p2 " +
            "JOIN p2.slots c2 " +
            "JOIN c2.closedSlots cs " +
            "WHERE p2.id = :productId " +
            "AND p2.status = 'ACTIVE' " +
            "AND c2.dayOfWeek = :dayOfWeek " +
            "AND os.startDate = cs.startDate" +
            ")")
    List<TimeSlotEntity> findSlotsForDayOfWeek(@Param("productId") UUID productId, @Param("dayOfWeek") DayOfWeek dayOfWeek);

    Optional<ProductEntity> findById(UUID id);
}
