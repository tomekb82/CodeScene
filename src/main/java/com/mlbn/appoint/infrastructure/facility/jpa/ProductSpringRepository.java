package com.mlbn.appoint.infrastructure.facility.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface ProductSpringRepository extends JpaRepository<ProductEntity, UUID> {

    @Query("SELECT DISTINCT os FROM ProductEntity p " +
            "JOIN p.configurationSlots c " +
            "JOIN c.slots os " +
            "WHERE p.id = :productId " +
                "AND p.status = 'ACTIVE' " +
                "AND c.dayOfWeek = :dateOfWeek " +
                "AND NOT EXISTS (" +
                    "SELECT 1 FROM ProductEntity p2 " +
                    "JOIN p2.closedSlots c2 " +
                    "JOIN c2.slots cs " +
                    "WHERE p2.id = :productId " +
                        "AND p2.status = 'ACTIVE' " +
                        "AND CAST(c2.date AS date) = :date " +
                        "AND ((os.startTime >= cs.startTime AND os.startTime <= cs.endTime) " +
                            "OR (os.endTime >= cs.startTime AND os.endTime <= cs.endTime)" +
                            "OR (os.startTime <= cs.startTime AND os.startTime <= cs.endTime AND os.endTime >= cs.endTime))" +
            ")")
    List<TimeSlotEntity> findOpenSlots(@Param("productId") UUID productId,
                                       @Param("date") LocalDate date,
                                       @Param("dateOfWeek") DayOfWeek dateOfWeek);

    Optional<ProductEntity> findById(UUID id);
}
