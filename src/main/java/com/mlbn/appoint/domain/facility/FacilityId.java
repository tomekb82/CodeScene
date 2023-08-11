package com.mlbn.appoint.domain.facility;

import com.mlbn.appoint.shared.validation.Failure;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.util.UUID;

import static com.mlbn.appoint.shared.validation.Validator.isNotNull;

public record FacilityId(UUID id) {

    private static final String NO_FACILITY_ID_ERROR = "Facility ID is required!";

    public static Validation<Seq<Failure>, FacilityId> validate(FacilityId facilityId) {
        return Validation.combine(
                isNotNull(facilityId, NO_FACILITY_ID_ERROR),
                isNotNull(facilityId, NO_FACILITY_ID_ERROR).isValid()
                        ? isNotNull(facilityId.id(), NO_FACILITY_ID_ERROR)
                        : Validation.invalid(Failure.of(NO_FACILITY_ID_ERROR))
        ).ap((id, ignore) -> id);
    }
}
