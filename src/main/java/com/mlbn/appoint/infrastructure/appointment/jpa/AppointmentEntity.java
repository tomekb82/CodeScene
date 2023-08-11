package com.mlbn.appoint.infrastructure.appointment.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name ="appointments")
class AppointmentEntity{

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID clientId;

    @Column(nullable = false)
    private UUID facilityId;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private LocalDateTime slotStartDate;

    @Column(nullable = false)
    private String slotDuration;

    @Column(nullable = false)
    private UUID employeeId;

    private String comment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    enum Status {
        PENDING, CONFIRMED, CANCELED
    }

    public boolean isConfirmed() {
       return status == Status.CONFIRMED;
    }
}
