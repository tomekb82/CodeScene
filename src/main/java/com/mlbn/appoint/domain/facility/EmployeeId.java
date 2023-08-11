package com.mlbn.appoint.domain.facility;

import com.mlbn.appoint.shared.validation.Failure;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.util.UUID;

import static com.mlbn.appoint.shared.validation.Validator.isNotNull;

public record EmployeeId(UUID id) {

    private static final String NO_SERVICE_EMPLOYEE_ERROR = "Service employee ID is required!";

    public static Validation<Seq<Failure>, EmployeeId> validate(EmployeeId employeeId) {
        return Validation.combine(
                isNotNull(employeeId, NO_SERVICE_EMPLOYEE_ERROR),
                isNotNull(employeeId, NO_SERVICE_EMPLOYEE_ERROR).isValid()
                        ? isNotNull(employeeId.id(), NO_SERVICE_EMPLOYEE_ERROR)
                        : Validation.invalid(Failure.of(NO_SERVICE_EMPLOYEE_ERROR))
        ).ap((id, ignore) -> id);
    }
}
