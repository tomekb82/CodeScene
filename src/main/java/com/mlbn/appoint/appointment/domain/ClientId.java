package com.mlbn.appoint.appointment.domain;

import com.mlbn.appoint.common.validation.Failure;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.util.UUID;

import static com.mlbn.appoint.common.validation.Validator.isNotNull;

public record ClientId(UUID id) {

    private static final String NO_CLIENT_ID_ERROR = "Client ID is required!";

    public static Validation<Seq<Failure>, ClientId> validate(ClientId clientId) {
        return Validation.combine(
                isNotNull(clientId, NO_CLIENT_ID_ERROR),
                isNotNull(clientId, NO_CLIENT_ID_ERROR).isValid()
                        ? isNotNull(clientId.id(), NO_CLIENT_ID_ERROR)
                        : Validation.invalid(Failure.of(NO_CLIENT_ID_ERROR))
        ).ap((id, ignore) -> id);
    }
}
