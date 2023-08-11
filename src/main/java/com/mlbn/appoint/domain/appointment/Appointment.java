package com.mlbn.appoint.domain.appointment;

import com.mlbn.appoint.shared.validation.Failure;
import com.mlbn.appoint.domain.facility.EmployeeId;
import com.mlbn.appoint.domain.facility.FacilityId;
import com.mlbn.appoint.shared.vo.ClientId;
import com.mlbn.appoint.shared.vo.Comment;
import io.vavr.control.Either;
import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.util.UUID;

import static com.mlbn.appoint.shared.validation.Results.*;
import static com.mlbn.appoint.shared.validation.Validator.isNotNull;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Appointment {

    private AppointmentId id;
    private ClientId clientId;
    private FacilityId facilityId;
    private BookingSlot slot;
    private EmployeeId employeeId;
    private Comment comment;
    @With(AccessLevel.PRIVATE)
    private Status status;

    public enum Status {
        PENDING, CONFIRMED, CANCELED;

        private static final String NO_APPOINTMENT_STATUS_ERROR = "Appointment status is required!";
        public static Validation<Failure, Status> validate(Status status) {
            return isNotNull(status, NO_APPOINTMENT_STATUS_ERROR);
        }
    }

    static Either<Failure, Appointment> book(BookingCommand command) {
        return success(from(command).withStatus(Status.CONFIRMED));
    }

    Either<Failure, Appointment> cancel() {
        return isConfirmed()
            ? success(withStatus(Status.CANCELED))
            : failure(String.format("Wrong status, status = %s", status));
    }

    private boolean isConfirmed() {
        return status == Status.CONFIRMED;
    }

    private static Appointment from(BookingCommand command){
        return builder()
                .id(new AppointmentId(UUID.randomUUID()))
                .clientId(command.getClientId())
                .facilityId(command.getFacilityId())
                .slot(command.getBookingSlot())
                .employeeId(command.getEmployeeId())
                .comment(command.getComment())
                .build();
    }

    public static Appointment fromExisting(FromExisitngCommand command){
        return builder()
                .id(command.getAppointmentId())
                .clientId(command.getClientId())
                .facilityId(command.getFacilityId())
                .slot(command.getServiceSlot())
                .employeeId(command.getEmployeeId())
                .comment(command.getComment())
                .status(command.getStatus())
                .build();
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id.id() +
                ", clientId=" + clientId.id() +
                ", facilityId=" + facilityId.id() +
                ", productId=" + slot.productId().id() +
                ", slot start date=" + slot.startDate() +
                ", slot duration=" + slot.duration() +
                ", employeeId=" + employeeId.id() +
                ", comment=" + comment.text() +
                ", status=" + status +
                '}';
    }
}
