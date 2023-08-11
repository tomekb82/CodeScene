package com.mlbn.appoint.shared.vo;

import com.mlbn.appoint.shared.validation.Failure;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

import java.util.UUID;

import static com.mlbn.appoint.shared.validation.Validator.isNotNull;

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
